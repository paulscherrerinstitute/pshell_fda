/*
 * Copyright (c) 2014 Paul Scherrer Institute. All rights reserved.
 */

package ch.psi.fda;

import ch.psi.pshell.core.Context;
import ch.psi.pshell.core.ExecutionParameters;
import ch.psi.pshell.data.DataManager;
import ch.psi.pshell.data.LayoutTable;
import ch.psi.pshell.data.PlotDescriptor;
import ch.psi.pshell.data.Provider;
import ch.psi.pshell.data.ProviderText;
import ch.psi.pshell.scan.Scan;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public class LayoutFDA extends LayoutTable {
    static String filePrefix = null;
            
    public static String getFilePrefix(){
        if (filePrefix == null){
            String dataPath = Context.getInstance().getConfig().dataPath;
            if (dataPath.contains("{name}")){ 
                try{
                    dataPath = dataPath.replaceAll("./$", ""); //remove last / if present
                    String[] tokens  = dataPath.split("/");
                    return tokens[tokens.length-1];                
                } catch (Exception ex){                    
                }
            }
            return "{date}_{time}_{name}";            
        }
        return filePrefix;
    }

    public static void setFilePrefix(String filePrefix){
        LayoutFDA.filePrefix = filePrefix;
    }
    
    
    @Override
    protected String getLogFileName() {        
        ExecutionParameters pars = Context.getInstance().getExecutionPars();
        return Context.getInstance().getSetup().expandPath(getFilePrefix(), pars.getStart())+".log";
    }
    
    @Override
    protected String getDatasetName(Scan scan) {        
        ExecutionParameters pars = Context.getInstance().getExecutionPars();
        return  Context.getInstance().getSetup().expandPath(getFilePrefix()+ "_" + String.format("%04d",(pars.getCount()-1)), pars.getStart());                        
    }    
    
    @Override
    public List<PlotDescriptor> getScanPlots(String root, String path, DataManager dm) throws IOException {
        if (isFdaDataFile(root, path)){
            throw new IOException("Let FDA make the plotting");
        }
        return super.getScanPlots(root, path, dm);
    }
    
    public static boolean isFdaDataFile(String root, String path) throws IOException{
        if (!Context.getInstance().getDataManager().isDataset(root, path)) {
            return false;
        }        
        Provider p = Context.getInstance().getDataManager().getProvider();
        if (!(p instanceof ProviderText)){
            return false;
        }
        Path filePath = ((ProviderText)p).getFilePath(root, path);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String first = br.readLine(); 
            String second = br.readLine();
            String third = br.readLine();
            if ((first.startsWith("#")) && (second.startsWith("#")) && (!third.startsWith("#"))){
                for (String token: second.substring(1).split(((ProviderText)p).getItemSeparator())){
                    Integer.valueOf(token.trim());
                }
                return true;
            }
        } catch (Exception ex) {            
        }
        return false;
    }
    
    
    //FDA layout doesn' save global attributes
    @Override
    public void onOpened(File output) throws IOException {
    } 
    
    @Override
    public void onClosed(File output) throws IOException {
    }     
}
