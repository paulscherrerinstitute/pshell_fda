package ch.psi.fda;

import ch.psi.fda.aq.Acquisition;
import ch.psi.fda.aq.AcquisitionConfiguration;
import ch.psi.fda.aq.NotificationAgent;
import ch.psi.fda.aq.XScanDescriptorProvider;
import ch.psi.fda.deserializer.DataDeserializer;
import ch.psi.fda.deserializer.DataDeserializerTXT;
import ch.psi.fda.ui.ce.panels.model.ConfigurationPanel;
import ch.psi.fda.model.ModelManager;
import ch.psi.fda.model.v1.Configuration;
import ch.psi.fda.model.v1.Data;
import ch.psi.fda.model.v1.Recipient;
import ch.psi.fda.model.v1.Scan;
import ch.psi.fda.ui.ce.panels.model.util.ModelUtil;
import ch.psi.fda.ui.visualizer.Visualizer;
import ch.psi.fda.vdescriptor.VDescriptor;
import ch.psi.jcae.ChannelService;
import ch.psi.jcae.impl.DefaultChannelService;
import ch.psi.pshell.core.CommandSource;
import ch.psi.pshell.core.Context;
import ch.psi.pshell.plot.PlotBase;
import ch.psi.pshell.swing.PlotPanel;
import ch.psi.pshell.ui.App;
import ch.psi.pshell.ui.Preferences;
import ch.psi.utils.swing.MainFrame;
import ch.psi.pshell.ui.Task;
import ch.psi.utils.IO;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.File;
import ch.psi.utils.swing.MonitoredPanel;
import java.io.IOException;
import ch.psi.utils.swing.SwingUtils;
import java.awt.Component;
import ch.psi.pshell.ui.Processor;
import ch.psi.utils.State;
import ch.psi.utils.swing.ExtensionFileFilter;
import ch.psi.utils.swing.SwingUtils.OptionType;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Paths;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Loops can be executed from script as:    ProcessorFDA().execute("test1.xml")
 */
public final class ProcessorFDA extends MonitoredPanel implements Processor {
    static {
        
        JMenuBar menu = App.getInstance().getMainFrame().getMenu();
        JMenu menuView =  (JMenu) SwingUtils.getComponentByName(menu, "menuView");
        JMenuItem menuDataBrowser = new JMenuItem("FDA Browser");
        menuDataBrowser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
        menuView.insert(menuDataBrowser, menuView.getMenuComponentCount() -1);
        menuView.insertSeparator(menuView.getMenuComponentCount() -1);
        menuDataBrowser.addActionListener((e)-> {
            showDataBrowser();
        });
        SwingUtils.adjustMacMenuBarAccelerator(menuDataBrowser);          
        try {
            if ("true".equalsIgnoreCase(Context.getInstance().getSetting("FdaBrowser"))){
                showDataBrowser();
            }
        } catch (IOException ex) {            
        }
    }
    
    static JTabbedPane getDataBrowserTabbedPane(){
        return App.getInstance().getMainFrame().getLeftTab().isVisible() ? 
                App.getInstance().getMainFrame().getLeftTab() :
                App.getInstance().getMainFrame().getDocumentsTab();
    }
    
    static boolean isDataBrowserVisible(){
        JTabbedPane tab = getDataBrowserTabbedPane();                
        for (int i = 0; i < tab.getTabCount(); i++) {
            Component c = tab.getComponentAt(i);
            if (c instanceof DataBrowser) {
                tab.setSelectedComponent(c);
                return true;
            }
        }     
        return false;
    }
    
    static int dataBrowserInstances=0;
    static void showDataBrowser(){
        JTabbedPane tab = getDataBrowserTabbedPane();                
        for (int i = 0; i < tab.getTabCount(); i++) {
            Component c = tab.getComponentAt(i);
            if (c instanceof DataBrowser) {
                tab.setSelectedComponent(c);
                return;
            }
        }      
        DataBrowser panel = new DataBrowser();
        panel.setCached(App.getInstance().getMainFrame().getPreferences().cachedDataPanel);
        panel.initialize(null);        
        App.getInstance().getMainFrame().openComponent("FDA Browser", panel, tab);       
        dataBrowserInstances = Math.max(dataBrowserInstances+1, 1);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                dataBrowserInstances--;
                if (dataBrowserInstances<=0){
                    try {
                        Context.getInstance().setSetting("FdaBrowser", false);
                    } catch (IOException ex) {            
                    }                    
                }
            }
        });
        try {
            Context.getInstance().setSetting("FdaBrowser", true);
        } catch (IOException ex) {            
        }
    }
    
    private ConfigurationPanel panelConfig;
    final AcquisitionConfiguration configuration;
    final String homePath;

    public ProcessorFDA() {
        initComponents();
        configuration = new AcquisitionConfiguration();
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane1.getHorizontalScrollBar().setUnitIncrement(16);
        if ((App.getInstance()!=null) && (App.hasArgument("fdahome"))){
            homePath = Context.getInstance().getSetup().expandPath(App.getArgumentValue("fdahome"));
        } else {
            homePath = Context.getInstance().getSetup().getScriptPath();
        }
    }

    public ProcessorFDA(File file) throws IOException {
        this();
        if (file != null) {
            open(file.getAbsolutePath());
        }
        changed = false;
        ModelUtil.getInstance().clearModified();
    }

    public static ProcessorFDA getCurrent() {
        Processor p = App.getInstance().getMainFrame().getSelectedProcessor();
        if ((p != null) && (p instanceof ProcessorFDA)) {
            return (ProcessorFDA) p;
        }
        return null;
    }

    public ConfigurationPanel getConfigPanel() {
        return panelConfig;
    }

    @Override
    public String getType() {
        return "FDA";
    }

    @Override
    public String getDescription() {
        return "FDA configuration file  (*.xml)";
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"xml"};
    }
    
    @Override
    public boolean createMenuNew(){
        return true;
    }        

    @Override
    public void open(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.getName().endsWith("." + getExtensions()[0])) {
            file = new File(file.getAbsolutePath() + "." + getExtensions()[0]);
        }
        setFile(file);
        Configuration c = null;
        if (!file.exists()) {
            //New file
            c = new Configuration();
            c.setScan(new Scan());
            try {
                ModelManager.marshall(c, file);
            } catch (Exception ex) {
                throw new IOException(ex);
            }
        } else {
            try {
                c = ModelManager.unmarshall(file);
            } catch (Exception ex) {
                throw new IOException(ex);
            }

            // Check if scan name exists, if not, replace with name of the file
            if (c.getData() == null) {
                c.setData(new Data());
            }
            if (c.getData().getFileName() == null || c.getData().getFileName().trim().equals("")) {
                //c.getData().setFileName(dataObject.getPrimaryFile().getName());
                c.getData().setFileName(file.getName());
            }
        }

        this.jPanel1.removeAll();
        this.panelConfig = new ConfigurationPanel(c);
        this.jPanel1.add(panelConfig, BorderLayout.CENTER);
        this.jPanel1.revalidate();
        this.jPanel1.repaint();

    }

    File file;

    private void setFile(File f) {
        this.file = f;
        setName(IO.getPrefix(f.getName()));
    }
    
    @Override
    public JPanel getPanel() {
        //Creating new
        if (file==null){
            JFileChooser chooser = new JFileChooser(getHomePath());
            chooser.addChoosableFileFilter(new ExtensionFileFilter(getDescription(), getExtensions()));
            chooser.setDialogTitle("Enter new file name");
            if (chooser.showSaveDialog(SwingUtils.getFrame(this)) != JFileChooser.APPROVE_OPTION) {
                throw new RuntimeException("File name must be set");
            }

            try {   
                File f = chooser.getSelectedFile();
                if (!String.valueOf(IO.getExtension(f)).toLowerCase().equals("xml")){
                    f = new File(f.getCanonicalPath() + ".xml");
                }               
                if (f.exists()){
                    throw new RuntimeException("File already exists");
                }                
                open(f.getCanonicalPath());
                save();
                SwingUtilities.invokeLater(()->{
                    JTabbedPane tabDoc = App.getInstance().getMainFrame().getDocumentsTab();
                    tabDoc.setTitleAt(tabDoc.getSelectedIndex(), new File(getFileName()).getName());
                });
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return Processor.super.getPanel();
    }

    @Override
    public void save() throws IOException {
        File copy = null;

        try {
            if (this.file != null) {
                copy = new File(file.getAbsolutePath() + ".tmp");
                IO.copy(file.getAbsolutePath(), copy.getAbsolutePath());
            }
            ModelManager.marshall(panelConfig.getObject(), file);
            changed = false;
            ModelUtil.getInstance().clearModified();

        } catch (Exception ex) {
            if (copy != null) {
                file.delete();
                IO.copy(copy.getAbsolutePath(), file.getAbsolutePath());
                open(file.getAbsolutePath());
            }
            throw (ex instanceof IOException) ? (IOException) ex : new IOException(ex);
        } finally {
            if (copy != null) {
                copy.delete();
            }
        }
    }

    @Override
    public void saveAs(String fileName) throws IOException {
        Configuration model = panelConfig.getObject();

        try {
            File f = new File(fileName);
            ModelManager.marshall(model, f);
            setFile(f);
            changed = false;
            ModelUtil.getInstance().clearModified();
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public String getHomePath() {
        return homePath;        
    }

    boolean changed = false;

    @Override
    public boolean hasChanged() {
        changed = changed || ModelUtil.getInstance().wasModified();
        return changed;
    }

    @Override
    public boolean checkChangeOnClose() throws IOException {
        if (hasChanged() /*&& (!isReadOnly())*/) {
            switch (SwingUtils.showOption(this, "Closing", "Document has changed. Do you want to save it?", OptionType.YesNoCancel)) {
                case Yes:
                    save();
                    break;
                case No:
                    break;
                case Cancel:
                    return false;
            }
        }
        return true;
    }

    @Override
    public String getFileName() {
        return (file != null) ? file.getPath() : null;
    }

    @Override
    public void setEnabled(boolean value) {
        boolean hadChanged = hasChanged();  //setEnabled trigger JComboBox valueChange...
        super.setEnabled(value);
        for (Component c : SwingUtils.getComponentsByType(panelConfig, Component.class)) {
            c.setEnabled(value);
        }
        if (!hadChanged) {
            changed = false;
            ModelUtil.getInstance().clearModified();
        }
    }
    
    class FDATask extends Task {

        final String file;
        final Configuration configuration;

        public FDATask(String file, Configuration configuration) {
            super();
            this.file = file;
            this.configuration = configuration;
        }

        @Override
        protected String doInBackground() throws Exception {
            Context.getInstance().startExecution(CommandSource.ui, file, null, true);
            String msg = "Running " + getFileName();
            try {
                setEnabled(false);
                setMessage(msg);
                setProgress(0);
                App.getInstance().sendTaskInit(msg);
                doExecution(false, null);
                setProgress(100);
                return msg;
            } catch (InterruptedException ex) {
                App.getInstance().sendOutput("Execution aborted");
                throw ex;
            } catch (Exception ex) {
                App.getInstance().sendError(ex.toString());
                if (App.getInstance().getMainFrame().getPreferences().getScriptPopupDialog() != Preferences.ScriptPopupDialog.None) {
                    if (!Context.getInstance().isAborted()) {
                        SwingUtils.showMessage(App.getInstance().getMainFrame(), "Script Error", ex.getMessage(), -1, JOptionPane.ERROR_MESSAGE);
                    }
                }                
                throw ex;
            } finally {
                App.getInstance().sendTaskFinish(msg);
                Context.getInstance().endExecution();
                setEnabled(true);
            }
        }

        protected void _setProgress(int progress) {
            setProgress(progress);
        }
    }

    public static void setProgress(int progress) {
        if (App.getInstance()!=null){
            Task task = App.getInstance().getRunningTask();
            if ((task != null) && (task instanceof FDATask)) {
                ((FDATask) task)._setProgress(progress);
            }
        }
    }

    public static void setPlots(List<JPanel> plots, String title) {
        PlotPanel panel;
        if (App.getInstance().getMainFrame()!=null){
            panel = App.getInstance().getMainFrame().getPlotPanel(title);
        } else {
            panel = App.getInstance().getPlotPanel(title,null);
        }
        
        panel.clear();
        for (JPanel plot : plots) {
            panel.addPlot((PlotBase) plot);
        }
        if (App.getInstance().getMainFrame()!=null){
            App.getInstance().getMainFrame().getPlotsTab().setSelectedComponent(panel);
        }
        panel.validate();
        panel.repaint();                
    }

    public static void setIcon(JLabel c, URL url){
        try{
            if (MainFrame.isDark()){
                c.setIcon(new ImageIcon(SwingUtils.invert(Toolkit.getDefaultToolkit().getImage(url))));        
            } else {
                c.setIcon(new ImageIcon(url));
            }
        } catch (Throwable t){
            t.printStackTrace();
        }
    }

    public static void setIcon(JButton c, URL url){
        try{
            if (MainFrame.isDark()){
                c.setIcon(new ImageIcon(SwingUtils.invert(Toolkit.getDefaultToolkit().getImage(url))));        
            } else {
                c.setIcon(new ImageIcon(url));
            }
        } catch (Throwable t){
            t.printStackTrace();
        }
    }

    @Override
    public void execute() throws Exception {
        App.getInstance().startTask(new FDATask(getFileName(), panelConfig.getObject()));
    }

    @Override
    public void abort() throws InterruptedException{
        doAbort();
        if (App.getInstance() != null){
            App.getInstance().abort();
        }
    }
    
    @Override
    public boolean createFilePanel() {
        return true;
    }     

    public AcquisitionConfiguration getConfiguration() {
        return configuration;
    }
    
    @Override    
    public void execute(String file, Map<String, Object> vars) throws Exception{
        if (! new File(file).exists()){
           file = Paths.get(getHomePath(), file).toString();
        }
        if (! new File(file).exists()){
            throw new IOException("Invalid file: " + file);
        }
        open(file);
        
        State initialState = Context.getInstance().getState();
        if (initialState == State.Ready){
            Context.getInstance().startExecution(CommandSource.terminal, file, null, false);
        }
        String msg = "Running " + getFileName();
        try {
            doExecution(!App.isGui(), vars);
        } catch (Exception ex) {
            App.getInstance().sendError(ex.toString());
            throw ex;
        } finally {         
            if (initialState == State.Ready){
                Context.getInstance().endExecution();
            }
        }        
    }    

    public void testNotification(String recipient) {
        NotificationAgent a = new NotificationAgent(getConfiguration().getSmptServer(), "fda.test.notification@psi.ch");
        Recipient r = new Recipient();
        r.setValue(recipient);
        r.setError(true);
        r.setSuccess(true);
        a.getRecipients().add(r);
        a.sendNotification("FDA Test Notification", "This is a test notification from FDA", true, true);
    }

    Thread executionThread;
    Acquisition acquisition;

    void doExecution(boolean batch, Map<String, Object> vars) throws Exception {
        executionThread = Thread.currentThread();
        //System.gc();
        ChannelService channelService = new DefaultChannelService();
        Visualizer visualizer = null;
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {            
            setProgress(25);
            ModelUtil.getInstance().setConfigurationPanel(panelConfig);            
            EventBus ebus = new AsyncEventBus(executor);
            acquisition = new Acquisition(channelService, getConfiguration(), vars);
            Configuration c = panelConfig.getObject();            
            acquisition.initalize(ebus, c);
            if (!batch && App.isScanPlottingActive()){
                visualizer = new Visualizer(XScanDescriptorProvider.mapVisualizations(c.getVisualization()));
                if (c.getScan() != null && c.getScan().getCdimension() != null) {
                    // If there is a continuous dimension only update the plot a the end of a line.
                    // Improvement of performance
                    visualizer.setUpdateAtStreamElement(true);
                    visualizer.setUpdateAtStreamDelimiter(false);
                    visualizer.setUpdateAtEndOfStream(false);
                }
                ebus.register(visualizer);
                ProcessorFDA.setPlots(visualizer.getPlotPanels(), null);
            }
            acquisition.execute();
        } catch (InterruptedException ex) {
            throw ex;
        } catch (Throwable t) {
            Logger.getLogger(ProcessorFDA.class.getName()).log(Level.WARNING, null, t);
            t.printStackTrace();
            throw t;
        } finally {
            ModelUtil.getInstance().setConfigurationPanel(null);
            Logger.getLogger(ProcessorFDA.class.getName()).log(Level.FINER, "Destroy acquisition");
            if (acquisition != null) {
                acquisition.destroy();
                acquisition = null;
            }
                        
            try {
                channelService.destroy();
            } catch (Exception e) {
                Logger.getLogger(ProcessorFDA.class.getName()).log(Level.FINER, "Unable to destroy channel access service", e);
            }
            
            Logger.getLogger(ProcessorFDA.class.getName()).log(Level.FINER, "Stop visualizer");
            
            //TODO: giving more time to gert latest events. Is there a better way to know the event bus is empty?
            new Thread(new Runnable() {
                @Override
                public void run() {                        
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                    }
                    try {
                        executor.shutdown();
                    } catch (Exception e) {
                        Logger.getLogger(ProcessorFDA.class.getName()).log(Level.FINER, "Unable to stop executor service", e);
                    }
                }
            }).start();                                
            executionThread = null;
        }
    }

    void doAbort() {
        if (acquisition != null) {
            try {
                acquisition.abort();
            } catch (Exception e) {
            }
        }
        if (executionThread != null) {
            executionThread.interrupt();
        }
    }

    @Override
    public void plotDataFile(final File file) {

        Logger.getLogger(ProcessorFDA.class.getName()).log(Level.INFO, "Visualize file: {0}", file.getAbsolutePath());
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // Try to determine configuration file from data file name
                    File dir = file.getParentFile();
                    String name = file.getName();
                    name = name.replaceAll("_[0-9]*.txt$", "");
                    //If no suffix
                    name = name.replaceAll(".txt$", "");
                    File cfile = new File(dir, name + ".xml");

                    // Check existence of files
                    if (!file.exists()) {
                        throw new IllegalArgumentException("Data file [" + file.getAbsolutePath() + "] does not exist");
                    }
                    if (!cfile.exists()) {
                        throw new IllegalArgumentException("Configuration file [" + cfile.getAbsolutePath() + "] does not exist");
                    }

                    EventBus ebus = new AsyncEventBus(Executors.newSingleThreadExecutor());
                    DataDeserializer deserializer = new DataDeserializerTXT(ebus, file);

                    VDescriptor vdescriptor = null;
                    ServiceLoader<DescriptorProvider> providers = ServiceLoader.load(DescriptorProvider.class);
                    for (DescriptorProvider provider : providers) {
                        try {
                            provider.load(cfile);
                            vdescriptor = provider.getVDescriptor();
                            break;
                        } catch (Exception e) {
                            Logger.getLogger(ProcessorFDA.class.getName()).log(Level.INFO, provider.getClass().getName() + " is not able to read provided descriptor files", e);
                        }
                    }

                    Visualizer visualizer = new Visualizer(vdescriptor);
                    visualizer.setUpdateAtStreamElement(false);
                    visualizer.setUpdateAtStreamDelimiter(false);
                    visualizer.setUpdateAtEndOfStream(true);

                    ebus.register(visualizer);

                    //tc.updatePanel(visualizer.getPlotPanels());
                    ProcessorFDA.setPlots(visualizer.getPlotPanels(), "Data");

                    deserializer.read();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtils.showMessage(App.getInstance().getMainFrame(), "Error", "An error occured while visualizing '" + file.getName() + "':\n" + ex.getMessage());
                }
            }
        });
        t.start();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        if (hasChanged()) {
            try {
                if (App.getInstance().getMainFrame().getDocumentsTab().getSelectedComponent() == this) {
                    int index = App.getInstance().getMainFrame().getDocumentsTab().getSelectedIndex();
                    JTabbedPane tabDoc = App.getInstance().getMainFrame().getDocumentsTab();
                    tabDoc.setTitleAt(index, new File(getFileName()).getName() + "*");
                    SwingUtils.CloseButtonTabComponent tabComponent = (SwingUtils.CloseButtonTabComponent) tabDoc.getTabComponentAt(index);
                    tabComponent.updateUI();
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }//GEN-LAST:event_formMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
