package ch.psi.fda;

import ch.psi.pshell.data.Converter;
import ch.psi.pshell.data.DataSlice;
import ch.psi.pshell.data.Provider;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.lang.reflect.Array;
import java.util.Map;

public class ConverterMat implements Converter {

    @Override
    public String getName() {
        return "Matlab";
    }

    @Override
    public String getExtension() {
        return "mat";
    }

    @Override
    public void convert(DataSlice slice, Map<String, Object> info, Map<String, Object> attrs, File output) throws Exception {
        List<List<Object>> dlist = new ArrayList<List<Object>>();
        List<Class<?>> clist = new ArrayList<Class<?>>();

        int[] shape = slice.sliceShape;
        Class type = slice.dataType;
        String[] fieldNames = (String[]) info.get(Provider.INFO_FIELD_NAMES);

        for (int i = 0; i < shape[0]; i++) {
            Object record = Array.get(slice.sliceData, i);
            int recordSize = Array.getLength(record);
            if (i == 0) {
                for (int j = 0; j < recordSize; j++) {
                    dlist.add(new ArrayList<Object>());
                    clist.add(Array.get(record, j).getClass());
                }
            }
            for (int j = 0; j < recordSize; j++) {
                Object object = Array.get(record, j);
                dlist.get(j).add(object);
            }
        }

        // Create Matlab vectors
        ArrayList<MLArray> matlablist = new ArrayList<MLArray>();
        for (int t = 0; t < dlist.size(); t++) {
            List<Object> list = dlist.get(t);

            if (clist.get(t).isArray()) {
                // Array Handling
            } else if (clist.get(t).equals(Double.class)) {
                // Data is of type Double
                MLDouble darray = new MLDouble(escapeString(fieldNames[t]), (Double[]) list.toArray(new Double[list.size()]), 1);
                matlablist.add(darray);
            }

        }

        // Write Matlab file
        MatFileWriter writerr = new MatFileWriter();
        writerr.write(output, matlablist);

    }

    public static String escapeString(String value) {

        String evalue = value.replaceAll("-", "_");
        evalue = evalue.replaceAll(":", "_");
        evalue = evalue.replaceAll("\\.", "_");
        evalue = evalue.replaceAll(" ", "_");
        evalue = evalue.replaceAll("\\(", "_");
        evalue = evalue.replaceAll("\\)", "_");
        evalue = evalue.replaceAll("\\[", "_");
        evalue = evalue.replaceAll("\\]", "_");

        return (evalue);
    } 
}
