import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DonorAnalysis {
    String inputFilePath;
    String outputZipFilePath;
    String outputDateFilePath;

    HashMap<RecipientZipcode, ContributionWithZip> donationWithZip = new HashMap<RecipientZipcode, ContributionWithZip>();
    TreeMap<String, TreeMap<Date, ContributionWithDate>> donationWithDate = new TreeMap<String, TreeMap<Date, ContributionWithDate>>();


    public DonorAnalysis(String inputFilePath, String outputZipFilePath, String outputDateFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputZipFilePath = outputZipFilePath;
        this.outputDateFilePath = outputDateFilePath;
    }


    public void analyse() {
        BufferedReader br = null;
        BufferedWriter bwZip = null;
        BufferedWriter bwDate = null;
        String recipientID;
        String zipCode;
        String dateStr;
        float amount = 0;
        String strAmount;
        try {
            bwZip = new BufferedWriter(new FileWriter(outputZipFilePath));
            bwDate = new BufferedWriter(new FileWriter(outputDateFilePath));
            br = new BufferedReader(new FileReader(inputFilePath));
            for (String line; (line = br.readLine()) != null; ) {
                // process the line
                String[] columns = line.split("\\|");
                // process other id
                if (!columns[15].isEmpty())
                    continue;
                // process recipient
                if ((recipientID = columns[0]).trim().isEmpty()) continue;
                // process amount
                strAmount = columns[14].trim();
                if (strAmount.isEmpty()) {
                    continue;
                } else {
                    try {
                        amount = Float.parseFloat(strAmount);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
                // process zip code
                zipCode = columns[10].trim();
                if (zipCode.length() >= 5) {
                    zipCode = zipCode.substring(0, 5);
                    boolean valid = true;
                    for(int i = 0; i < 5; i++){
                        if(!Character.isLetterOrDigit(zipCode.charAt(i))){
                            valid = false;
                            break;
                        }
                    }

                    if(!valid) continue;
                    // add record
                    ContributionRecord record = addContributionWithZip(recipientID, zipCode, amount);
                    // output to median by zip file
                    outputMedianByZip(bwZip, recipientID, zipCode, record);
                }
                // process date
                dateStr = columns[13].trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
//                if (dateStr.length() != dateFormat.toPattern().length()) continue;
                try {
                    Date date = dateFormat.parse(dateStr);
                    addContributionWithDate(recipientID, date, amount);
                } catch (ParseException e) {
                    continue;
                }
            }
            // output to median by date file
            outputMedianByDate(bwDate);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bwZip.close();
                bwDate.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ContributionRecord addContributionWithZip(String recipientID, String zipCode, float amount) {
        RecipientZipcode recipientZip = new RecipientZipcode(recipientID, zipCode);
        if (donationWithZip.containsKey(recipientZip)) {
            return donationWithZip.get(recipientZip).addAmount(amount);
        } else {
            ContributionWithZip contribution = new ContributionWithZip();
            donationWithZip.put(recipientZip, contribution);
            return contribution.addAmount(amount);
        }
    }


    public void outputMedianByZip(BufferedWriter bw, String recipientID, String zipCode,
                                  ContributionRecord record) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(recipientID).append("|")
                .append(zipCode).append("|")
                .append(record.median).append("|")
                .append(record.totalContributionNumber)
                .append("|").append(Math.round(record.totalDollarAmount)).append("\n");
        bw.write(sb.toString());
    }


    public void addContributionWithDate(String recipientID, Date date, float amount) {
        if (donationWithDate.containsKey(recipientID)) {
            TreeMap<Date, ContributionWithDate> dateMap = donationWithDate.get(recipientID);
            if (dateMap.containsKey(date)) {
                dateMap.get(date).addAmount(amount);
            } else {
                ContributionWithDate donation = new ContributionWithDate();
                donation.addAmount(amount);
                dateMap.put(date, donation);
            }
        } else {
            ContributionWithDate donation = new ContributionWithDate();
            donation.addAmount(amount);
            TreeMap<Date, ContributionWithDate> dateMap = new TreeMap<Date, ContributionWithDate>();
            dateMap.put(date, donation);
            donationWithDate.put(recipientID, dateMap);
        }
    }

    public void outputMedianByDate(BufferedWriter bw) throws IOException {
        String recipientID;
        Date date;
        ContributionRecord record;
        SimpleDateFormat format = new SimpleDateFormat("MMddyyyy");
        Iterator<Map.Entry<String, TreeMap<Date, ContributionWithDate>>> recipientIter = donationWithDate.entrySet().iterator();
        while (recipientIter.hasNext()) {
            Map.Entry<String, TreeMap<Date, ContributionWithDate>> ent = recipientIter.next();
            recipientID = ent.getKey().toString();
            TreeMap<Date, ContributionWithDate> dateMap = ent.getValue();
            Iterator<Date> dateIter = dateMap.keySet().iterator();
            while (dateIter.hasNext()) {
                date = dateIter.next();
                record = dateMap.get(date).getMedian();
                StringBuilder sb = new StringBuilder();
                sb.append(recipientID).append("|")
                        .append(format.format(date)).append("|")
                        .append(record.median).append("|")
                        .append(record.totalContributionNumber)
                        .append("|").append(Math.round(record.totalDollarAmount)).append("\n");
                bw.write(sb.toString());
            }
        }
    }

    public static void main(String args[]) {
        long start = System.currentTimeMillis();
        if (args.length == 3) {
            DonorAnalysis analysis = new DonorAnalysis(args[0], args[1], args[2]);
            analysis.analyse();
        } else {
            System.out.println("Please specify input file and out file paths.");
        }
        long end = System.currentTimeMillis();

        System.out.print(end - start);
    }
}
