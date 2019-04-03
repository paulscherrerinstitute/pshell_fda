/*
 * Copyright (c) 2014 Paul Scherrer Institute. All rights reserved.
 */

package ch.psi.fda;

import ch.psi.pshell.core.Context;
import ch.psi.pshell.core.ExecutionParameters;
import ch.psi.pshell.data.DataSlice;
import static ch.psi.pshell.data.Provider.INFO_DIMENSIONS;
import static ch.psi.pshell.data.Provider.INFO_FIELDS;
import ch.psi.pshell.data.ProviderText;
import static ch.psi.pshell.data.ProviderText.COMMENT_MARKER;
import ch.psi.utils.Convert;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ProviderFDA extends ProviderText{
    
    public ProviderFDA(){
        super();
        this.setItemSeparator("\t");
    }
    @Override
    protected Path getAttibutePath(String root, String path) throws IOException {
        ExecutionParameters pars =  Context.getInstance().getExecutionPars();
        if (pars!=null){
            return isGroup(root, path)
                    ? Paths.get(root, path, Context.getInstance().getSetup().expandPath("{date}_{time}_{name}." + ATTR_FILE))
                    : Paths.get(root, new File(path).getParent() + Context.getInstance().getSetup().expandPath("{date}_{time}_{name}_") + 
                                    new File(path).getName() + "." + ATTR_FILE);
        }
        return super.getAttibutePath(root, path);
    }
        
    @Override
    public DataSlice getData(String root, String path, int page) throws IOException {
        if (!LayoutFDA.isFdaDataFile( root, path)){
            return super.getData(root, path, page);
        } 
        
        DataSlice ret = null;
        if (!isDataset(root, path)) {
            return null;
        }

        Map<String, Object> info = getInfo(root, path);
        Integer fields = (Integer) info.get(INFO_FIELDS);
        String[] typeNames = (String[]) info.get(INFO_FIELD_TYPES);
        Path filePath = getFilePath(root, path);

        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            ArrayList data = new ArrayList();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (!line.startsWith(COMMENT_MARKER)) {
                        String[] vals = line.split(getItemSeparator());
                        if (fields != null) {
                            Object[] record = new Object[fields];
                            for (int i = 0; i < fields; i++) {
                                if (typeNames[i].equals(double[].class.getName())) {
                                    record[i] = Convert.toPrimitiveArray(vals[i], getArraySeparator(), double.class);                                            
                                } else {
                                    record[i] = Double.valueOf(vals[i]);
                                }
                            }
                            data.add(record);
                        } 
                    } 
                }
            }
            Object array = data.toArray(new Object[0][0]);
            return  new DataSlice(root, path, (int[]) info.get(INFO_DIMENSIONS), array, page, false);
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException("Invalid file format");
        }
    }
    
    @Override
    public Map<String, Object> getInfo(String root, String path) throws IOException {
        Map<String, Object> ret = super.getInfo(root, path);
        if (!LayoutFDA.isFdaDataFile( root, path)){
            return ret;
        } 
        
        Integer fields = (Integer) ret.get(INFO_FIELDS);
        if (fields!=null){
            Path filePath = getFilePath(root, path);
            String[] typeNames = new String[fields];
            int[] fieldLengths = new int[fields];
            if (filePath.toFile().isFile()) {
                try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
                    br.readLine();
                    br.readLine();
                    String line = br.readLine();

                    if (!line.isEmpty()) {
                        String[] vals = line.split(getItemSeparator());
                        if (fields != null) {
                            Object[] record = new Object[fields];
                            for (int i = 0; i < fields; i++) {
                                typeNames[i] = vals[i].contains(getArraySeparator()) ? double[].class.getName() : Double.class.getName();     
                                if (vals[i].contains(getArraySeparator())){
                                    if (vals[i].startsWith(getArraySeparator())){
                                        vals[i] = vals[i].substring(getArraySeparator().length());
                                    }
                                    fieldLengths[i] = vals[i].split(getArraySeparator()).length;
                                } else {
                                    fieldLengths[i] = 0;
                                }
                            }
                        }
                    }
                    ret.put(INFO_FIELD_TYPES, typeNames);
                    ret.put(INFO_FIELD_LENGTHS, fieldLengths);
                } catch (Exception ex){                
                }
            }
        }
        return ret;
    }
}