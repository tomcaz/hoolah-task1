package org.interview.hoolah;

import org.apache.commons.io.FileUtils;
import org.interview.hoolah.model.Transactions;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportService {
    String delimiter = ",";
    SimpleDateFormat inputFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    Map<String, Map<String,Transactions>> data = new HashMap<>();

    public List<String> setFile(File f) throws Exception {
        data.clear();
        List<String> merchants = new ArrayList<>();
        List<String> lines = FileUtils.readLines(f, Charset.defaultCharset());
        for (int i = 1; i < lines.size(); i++) { // skip first line for header
            String line = lines.get(i);
//            System.out.println(line);

            StringTokenizer stkr = new StringTokenizer(line,delimiter);
            String id = stkr.nextToken();
            Date date =
                    inputFormatter.parse(stkr.nextToken());
            String dec = stkr.nextToken();
            dec=dec.replaceAll(" ","");
            BigDecimal amount = new BigDecimal(dec);
            String merchant = stkr.nextToken().replaceAll(" ","");
            String type = stkr.nextToken();
            String relatedTransactionId = stkr.hasMoreTokens()?stkr.nextToken().replaceAll(" ",""):"";
            Transactions tran = new Transactions(
                    id, date, amount, merchant, type, relatedTransactionId
            );
            if(tran.isReversal()){ // to remove existing data
                data.get(merchant).remove(relatedTransactionId);
                continue;
            }
//            System.out.println(tran);
            if(!data.containsKey(merchant)){ // initialize array list if merchant is not exists in map
                data.put(merchant,new HashMap<>());
            }
            data.get(merchant).put(id,tran);

            if(!merchants.contains(merchant)){
                merchants.add(merchant);
            }
        }

        return merchants;
    }

    public Map<String,Object> generate(Date fromDate,Date toDate,String merchant){

        int numberOfTrans = 0;
        BigDecimal avg = new BigDecimal(0.0);

        for(int i = 0;i<data.get(merchant).size();i++){
            String key = data.get(merchant).keySet().toArray()[i].toString();
            Transactions transactions = data.get(merchant).get(key);
            if(transactions!= null && fromDate.before(transactions.getTransDate()) && (toDate.after(transactions.getTransDate()) || toDate.equals(transactions.getTransDate()))){
                System.out.println(transactions);
                numberOfTrans += 1;
                avg = avg.add(transactions.getAmount());
            }
        }

        Map<String,Object> hash = new HashMap<>();
        System.out.println(avg);
        System.out.println(numberOfTrans);
        hash.put("avg",numberOfTrans == 0? new BigDecimal(0.0):avg.divide(new BigDecimal(numberOfTrans)));
        hash.put("trans",numberOfTrans);
        return hash;
    }

    public boolean isEmpty(){
        return data == null || data.isEmpty();
    }
}
