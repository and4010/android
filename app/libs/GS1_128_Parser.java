package com.example.b02u002.yuho.utilities;

import androidx.core.app.NavUtils;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by B02U003 on 2017/12/18.
 */

public class GS1_128_Parser {
    public final static int UNIT_KG = 1;
    public final static int UNIT_LB = 2;
    public final static int UNIT_UNKNOWN = 0;
    private static GS1_128_Parser ean128Parser = new GS1_128_Parser();
    private static BarcodeData barcodeData;

    public enum DataType {
        Numeric,
        Alphanumeric
    }

    /// <summary>
    /// Information Class for an Application Identifier (AI)
    /// </summary>
    private class AII {
//        public String AI { get; set; }
//        public String Description { get; set; }
//        public int LengthOfAI { get; set; }
//        public DataType DataDescription { get; set; }
//        public int LengthOfData { get; set; }
//        public boolean FNC1 { get; set; }

        private String AI;
        private String Description;
        private int LengthOfAI;
        private DataType DataDescription;
        private int LengthOfData;
        private boolean FNC1;

        private AII(String AI, String Description, int LengthOfAI, DataType DataDescription, int LengthOfData, boolean FNC1) {
            this.AI = AI;
            this.Description = Description;
            this.LengthOfAI = LengthOfAI;
            this.DataDescription = DataDescription;
            this.LengthOfData = LengthOfData;
            this.FNC1 = FNC1;
        }
//        public override string ToString()
//    {
//        return String.Format("{0} [{1}]", AI, Description);
//    }
    }

    public static class BarcodeData{
        public String GTIN = null;
        public double Weight = 0;
        public int Weight_Unit = GS1_128_Parser.UNIT_UNKNOWN; //2磅 1公斤 0無(初始值)
        public Date PackingDate = null;
        public String Serial = null;
        public Date ProducerDate = null;
//        private BarcodeData(String GTIN, float Weight, int Weight_Unit, Date PackingDate, String Serial) {
//            this.GTIN = GTIN;
//            this.Weight = Weight;
//            this.Weight_Unit = Weight_Unit;
//            this.PackingDate = PackingDate;
//            this.Serial = Serial;
//        }
    }

    private Map<String, AII> aiiDict = new HashMap<String, AII>();
    private String[] aiis;
    private int minLengthOfAI = 99;
    private int maxLengthOfAI = 1;
    private char groutSeperator = (char) 29;
    private String ean128StartCode = "]C1";
    private boolean hasCheckSum = false;
    private int barcodeIndex = 0;

    //    public static boolean HasCheckSum
//    {
//        get { return EAN128Parser.hasCheckSum; }
//        set { EAN128Parser.hasCheckSum = value; }
//    }
    private boolean GetHasCheckSum() {
        return hasCheckSum;
    }
//    public static void SetHasCheckSum(boolean hasCheckSum){
//        EAN128Parser.hasCheckSum = hasCheckSum;
//    }


    //    public static char GroutSeperator
//    {
//        get { return EAN128Parser.groutSeperator; }
//        set { EAN128Parser.groutSeperator = value; }
//    }
    private char GetGroutSeperator() {
        return groutSeperator;
    }
//    public static void SetGroutSeperator(char groutSeperator){
//        EAN128Parser.groutSeperator = groutSeperator;
//    }


    //    public static String EAN128StartCode
//    {
//        get { return EAN128Parser.ean128StartCode; }
//        set { EAN128Parser.ean128StartCode = value; }
//    }
    private String GetEAN128StartCode() {
        return ean128StartCode;
    }
//    public static void SetEAN128StartCode(String ean128StartCode){
//        EAN128Parser.ean128StartCode = ean128StartCode;
//    }


    private GS1_128_Parser() {
        Add("00", "SerialShippingContainerCode", 2, DataType.Numeric, 18, false);
        Add("01", "EAN-NumberOfTradingUnit", 2, DataType.Numeric, 14, false);
        Add("02", "EAN-NumberOfTheWaresInTheShippingUnit", 2, DataType.Numeric, 14, false);
        Add("10", "Charge_Number", 2, DataType.Alphanumeric, 20, true);
        Add("11", "ProducerDate_JJMMDD", 2, DataType.Numeric, 6, false);
        Add("12", "DueDate_JJMMDD", 2, DataType.Numeric, 6, false);
        Add("13", "PackingDate_JJMMDD", 2, DataType.Numeric, 6, false);
        Add("15", "MinimumDurabilityDate_JJMMDD", 2, DataType.Numeric, 6, false);
        Add("17", "ExpiryDate_JJMMDD", 2, DataType.Numeric, 6, false);
        Add("20", "ProductModel", 2, DataType.Numeric, 2, false);
        Add("21", "SerialNumber", 2, DataType.Alphanumeric, 20, true);
        Add("22", "HIBCCNumber", 2, DataType.Alphanumeric, 29, false);
        Add("240", "PruductIdentificationOfProducer", 3, DataType.Alphanumeric, 30, true);
        Add("241", "CustomerPartsNumber", 3, DataType.Alphanumeric, 30, true);
        Add("250", "SerialNumberOfAIntegratedModule", 3, DataType.Alphanumeric, 30, true);
        Add("251", "ReferenceToTheBasisUnit", 3, DataType.Alphanumeric, 30, true);
        Add("252", "GlobalIdentifierSerialisedForTrade", 3, DataType.Numeric, 2, false);
        Add("30", "AmountInParts", 2, DataType.Numeric, 8, true);
        Add("310d", "NetWeight_Kilogram", 4, DataType.Numeric, 6, false);
        Add("311d", "Length_Meter", 4, DataType.Numeric, 6, false);
        Add("312d", "Width_Meter", 4, DataType.Numeric, 6, false);
        Add("313d", "Heigth_Meter", 4, DataType.Numeric, 6, false);
        Add("314d", "Surface_SquareMeter", 4, DataType.Numeric, 6, false);
        Add("315d", "NetVolume_Liters", 4, DataType.Numeric, 6, false);
        Add("316d", "NetVolume_CubicMeters", 4, DataType.Numeric, 6, false);
        Add("320d", "NetWeight_Pounds", 4, DataType.Numeric, 6, false);
        Add("321d", "Length_Inches", 4, DataType.Numeric, 6, false);
        Add("322d", "Length_Feet", 4, DataType.Numeric, 6, false);
        Add("323d", "Length_Yards", 4, DataType.Numeric, 6, false);
        Add("324d", "Width_Inches", 4, DataType.Numeric, 6, false);
        Add("325d", "Width_Feed", 4, DataType.Numeric, 6, false);
        Add("326d", "Width_Yards", 4, DataType.Numeric, 6, false);
        Add("327d", "Heigth_Inches", 4, DataType.Numeric, 6, false);
        Add("328d", "Heigth_Feed", 4, DataType.Numeric, 6, false);
        Add("329d", "Heigth_Yards", 4, DataType.Numeric, 6, false);
        Add("330d", "GrossWeight_Kilogram", 4, DataType.Numeric, 6, false);
        Add("331d", "Length_Meter", 4, DataType.Numeric, 6, false);
        Add("332d", "Width_Meter", 4, DataType.Numeric, 6, false);
        Add("333d", "Heigth_Meter", 4, DataType.Numeric, 6, false);
        Add("334d", "Surface_SquareMeter", 4, DataType.Numeric, 6, false);
        Add("335d", "GrossVolume_Liters", 4, DataType.Numeric, 6, false);
        Add("336d", "GrossVolume_CubicMeters", 4, DataType.Numeric, 6, false);
        Add("337d", "KilogramPerSquareMeter", 4, DataType.Numeric, 6, false);
        Add("340d", "GrossWeight_Pounds", 4, DataType.Numeric, 6, false);
        Add("341d", "Length_Inches", 4, DataType.Numeric, 6, false);
        Add("342d", "Length_Feet", 4, DataType.Numeric, 6, false);
        Add("343d", "Length_Yards", 4, DataType.Numeric, 6, false);
        Add("344d", "Width_Inches", 4, DataType.Numeric, 6, false);
        Add("345d", "Width_Feed", 4, DataType.Numeric, 6, false);
        Add("346d", "Width_Yards", 4, DataType.Numeric, 6, false);
        Add("347d", "Heigth_Inches", 4, DataType.Numeric, 6, false);
        Add("348d", "Heigth_Feed", 4, DataType.Numeric, 6, false);
        Add("349d", "Heigth_Yards", 4, DataType.Numeric, 6, false);
        Add("350d", "Surface_SquareInches", 4, DataType.Numeric, 6, false);
        Add("351d", "Surface_SquareFeet", 4, DataType.Numeric, 6, false);
        Add("352d", "Surface_SquareYards", 4, DataType.Numeric, 6, false);
        Add("353d", "Surface_SquareInches", 4, DataType.Numeric, 6, false);
        Add("354d", "Surface_SquareFeed", 4, DataType.Numeric, 6, false);
        Add("355d", "Surface_SquareYards", 4, DataType.Numeric, 6, false);
        Add("356d", "NetWeight_TroyOunces", 4, DataType.Numeric, 6, false);
        Add("357d", "NetVolume_Ounces", 4, DataType.Numeric, 6, false);
        Add("360d", "NetVolume_Quarts", 4, DataType.Numeric, 6, false);
        Add("361d", "NetVolume_Gallonen", 4, DataType.Numeric, 6, false);
        Add("362d", "GrossVolume_Quarts", 4, DataType.Numeric, 6, false);
        Add("363d", "GrossVolume_Gallonen", 4, DataType.Numeric, 6, false);
        Add("364d", "NetVolume_CubicInches", 4, DataType.Numeric, 6, false);
        Add("365d", "NetVolume_CubicFeet", 4, DataType.Numeric, 6, false);
        Add("366d", "NetVolume_CubicYards", 4, DataType.Numeric, 6, false);
        Add("367d", "GrossVolume_CubicInches", 4, DataType.Numeric, 6, false);
        Add("368d", "GrossVolume_CubicFeet", 4, DataType.Numeric, 6, false);
        Add("369d", "GrossVolume_CubicYards", 4, DataType.Numeric, 6, false);
        Add("37", "QuantityInParts", 2, DataType.Numeric, 8, true);
        Add("390d", "AmountDue_DefinedValutaBand", 4, DataType.Numeric, 15, true);
        Add("391d", "AmountDue_WithISOValutaCode", 4, DataType.Numeric, 18, true);
        Add("392d", "BePayingAmount_DefinedValutaBand", 4, DataType.Numeric, 15, true);
        Add("393d", "BePayingAmount_WithISOValutaCode", 4, DataType.Numeric, 18, true);
        Add("400", "JobNumberOfGoodsRecipient", 3, DataType.Alphanumeric, 30, true);
        Add("401", "ShippingNumber", 3, DataType.Alphanumeric, 30, true);
        Add("402", "DeliveryNumber", 3, DataType.Numeric, 17, false);
        Add("403", "RoutingCode", 3, DataType.Alphanumeric, 30, true);
        Add("410", "EAN_UCC_GlobalLocationNumber(GLN)_GoodsRecipient", 3, DataType.Numeric, 13, false);
        Add("411", "EAN_UCC_GlobalLocationNumber(GLN)_InvoiceRecipient", 3, DataType.Numeric, 13, false);
        Add("412", "EAN_UCC_GlobalLocationNumber(GLN)_Distributor", 3, DataType.Numeric, 13, false);
        Add("413", "EAN_UCC_GlobalLocationNumber(GLN)_FinalRecipient", 3, DataType.Numeric, 13, false);
        Add("414", "EAN_UCC_GlobalLocationNumber(GLN)_PhysicalLocation", 3, DataType.Numeric, 13, false);
        Add("415", "EAN_UCC_GlobalLocationNumber(GLN)_ToBilligParticipant", 3, DataType.Numeric, 13, false);
        Add("420", "ZipCodeOfRecipient_withoutCountryCode", 3, DataType.Alphanumeric, 20, true);
        Add("421", "ZipCodeOfRecipient_withCountryCode", 3, DataType.Alphanumeric, 12, true);
        Add("422", "BasisCountryOfTheWares_ISO3166Format", 3, DataType.Numeric, 3, false);
        Add("7001", "Nato Stock Number", 4, DataType.Numeric, 13, false);
        Add("8001", "RolesProducts", 4, DataType.Numeric, 14, false);
        Add("8002", "SerialNumberForMobilePhones", 4, DataType.Alphanumeric, 20, true);
        Add("8003", "GlobalReturnableAssetIdentifier", 4, DataType.Alphanumeric, 34, true);
        Add("8004", "GlobalIndividualAssetIdentifier", 4, DataType.Numeric, 30, true);
        Add("8005", "SalesPricePerUnit", 4, DataType.Numeric, 6, false);
        Add("8006", "IdentifikationOfAProductComponent", 4, DataType.Numeric, 18, false);
        Add("8007", "IBAN", 4, DataType.Alphanumeric, 30, true);
        Add("8008", "DataAndTimeOfManufacturing", 4, DataType.Numeric, 12, true);
        Add("8018", "GlobalServiceRelationNumber", 4, DataType.Numeric, 18, false);
        Add("8020", "NumberBillCoverNumber", 4, DataType.Alphanumeric, 25, false);
        Add("8100", "CouponExtendedCode_NSC_offerCcode", 4, DataType.Numeric, 10, false);
        Add("8101", "CouponExtendedCode_NSC_offerCcode_EndOfOfferCode", 4, DataType.Numeric, 14, false);
        Add("8102", "CouponExtendedCode_NSC", 4, DataType.Numeric, 6, false);
        Add("90", "InformationForBilateralCoordinatedApplications", 2, DataType.Alphanumeric, 30, true);
        //Add("91", "Company specific", 2, DataType.Alphanumeric, 30, true);
        //Add("92", "Company specific", 2, DataType.Alphanumeric, 30, true);
        //Add("93", "Company specific", 2, DataType.Alphanumeric, 30, true);
        //Add("94", "Company specific", 2, DataType.Alphanumeric, 30, true);
        //Add("95", "Company specific", 2, DataType.Alphanumeric, 30, true);
        //Add("96", "Company specific", 2, DataType.Alphanumeric, 30, true);
        //Add("97", "Company specific", 2, DataType.Alphanumeric, 30, true);
        //Add("98", "Company specific", 2, DataType.Alphanumeric, 30, true);
        //Add("99", "Company specific", 2, DataType.Alphanumeric, 30, true);
//        aiis = aiiDict.Keys.ToArray();
//        minLengthOfAI = aiiDict.Values.Min(el => el.LengthOfAI);
//        maxLengthOfAI = aiiDict.Values.Max(el => el.LengthOfAI);
//        minLengthOfAI = 2;
//        maxLengthOfAI = 4;
        for ( AII aii : aiiDict.values() ) {
            if (aii.LengthOfAI < minLengthOfAI){
                minLengthOfAI = aii.LengthOfAI;
            }
        }

        for ( AII aii : aiiDict.values() ) {
            if (aii.LengthOfAI > maxLengthOfAI){
                maxLengthOfAI = aii.LengthOfAI;
            }
        }
    }

    /// <summary>
    /// Add an Application Identifier (AI)
    /// </summary>
    /// <param name="AI">Number of the AI</param>
    /// <param name="Description"></param>
    /// <param name="LengthOfAI"></param>
    /// <param name="DataDescription">The type of the content</param>
    /// <param name="LengthOfData">The max lenght of the content</param>
    /// <param name="FNC1">Support a group seperator</param>
    private void Add(String AI, String Description, int LengthOfAI, DataType DataDescription, int LengthOfData, boolean FNC1) {
//        aiiDict[AI] = new AII(AI, Description, LengthOfAI, DataDescription, LengthOfData, FNC1);
        aiiDict.put(AI, new AII(AI, Description, LengthOfAI, DataDescription, LengthOfData, FNC1));
    }

    /// <summary>
    /// Parse the ean128 code
    /// </summary>
    /// <param name="data">The raw scanner data</param>
    /// <param name="throwException">If an exception will be thrown if an AI cannot be found</param>
    /// <returns>The different parts of the ean128 code</returns>
    private Map<String, String> Parse(String data) {
        // cut off the EAN128 start code
        if (data.startsWith(GetEAN128StartCode()))
            data = data.substring(GetEAN128StartCode().length());
//            data = data.Substring(GetEAN128StartCode().length());
        // cut off the check sum
        if (GetHasCheckSum())
            data = new String(data.getBytes(), 0, data.length() - 2);
//            data = data.Substring(0, data.Length - 2);

        Map<String, String> result = new HashMap<>();
        barcodeIndex = 0;
        // walkk through the EAN128 code
        while (barcodeIndex < data.length()) {
            // try to get the AI at the current position
            AII ai = GetAI(data, false);
            if (ai == null) {
//                if(throwException)
//                    throw new InvalidOperationException("AI not found");
                return result;
            }
            // get the data to the current AI
            String code = GetCode(data, ai);
//            result[ai] = code;
            result.put(ai.AI, code);
        }

        return result;
    }

    /// <summary>
    /// Try to get the AI at the current position
    /// </summary>
    /// <param name="data">The row data from the scanner</param>
    /// <param name="index">The refrence of the current position</param>
    /// <param name="usePlaceHolder">Sets if the last character of the AI should replaced with a placehoder ("d")</param>
    /// <returns>The current AI or null if no match was found</returns>
    private AII GetAI(String data, boolean usePlaceHolder) {
        AII result = null;
        // Step through the different lenghts of the AIs
        for (int i = minLengthOfAI; i <= maxLengthOfAI; i++) {
            if (barcodeIndex + i > data.length()) {
                break;
            }
            // get the AI sub string
//            String ai = data.Substring(index, i);
            String ai = new String(data.getBytes(), barcodeIndex, i);
            if (usePlaceHolder) {
                ai = ai.substring(0, ai.length() - 1) + "d";
            }
//                ai = ai.Remove(ai.Length - 1) + "d";
            // try to get the ai from the dictionary
//            if (aiiDict.TryGetValue(ai, out result))
            result = aiiDict.get(ai);
            if (result != null) {
                if (ai.substring(ai.length() - 1, ai.length()).equals("d")) {
                    result.AI = new String(data.getBytes(), barcodeIndex, i);
                }
                // Shift the index to the next
                barcodeIndex += i;
                return result;
            }
            // if no AI found, try it with the next lenght
        }
        // if no AI found here, than try it with placeholders. Assumed that is the first sep where usePlaceHolder is false
        if (!usePlaceHolder)
            result = GetAI(data, true);
        return result;
    }

    /// <summary>
    /// Get the current code to the AI
    /// </summary>
    /// <param name="data">The row data from the scanner</param>
    /// <param name="ai">The current AI</param>
    /// <param name="index">The refrence of the current position</param>
    /// <returns>the data to the current AI</returns>
    private String GetCode(String data, AII ai) {
        // get the max lenght to read.
        int lenghtToRead = Math.min(ai.LengthOfData, data.length() - barcodeIndex);
        // get the data of the current AI
//        String result = data.Substring(index, lenghtToRead);
        String result = new String(data.getBytes(), barcodeIndex, lenghtToRead);

        // check if the AI support a group seperator
        if (ai.FNC1) {
            // try to find the index of the group seperator
            int indexOfGroupTermination = result.indexOf(GetGroutSeperator());
            if (indexOfGroupTermination >= 0)
                lenghtToRead = indexOfGroupTermination + 1;
            // get the data of the current AI till the gorup seperator
//            result = data.Substring(index, lenghtToRead);
            result = new String(data.getBytes(), barcodeIndex, lenghtToRead);
        }

        // Shift the index to the next
        barcodeIndex += lenghtToRead;
        return result;
    }

    private String CodeTypeCheck(String code, AII ai) {
        //格式檢查
        //長度檢查
        return "";
    }

    public static BarcodeData BarcodeAnalyze(String barcode) {
        try {
            Map<String, String> data = ean128Parser.Parse(barcode);
            ean128Parser.ConversionBarcodeData(data);
        }
        catch (Exception ex){
            barcodeData = null;
            ex.getMessage();
//            Toast.makeText(MainActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
        return barcodeData;
    }

    private boolean BarcodeDataCheck(){
        if (barcodeData != null){
            if (barcodeData.GTIN == null){
                return false;
            }else if (barcodeData.Weight == 0) {
                return false;
            }else if (barcodeData.Weight_Unit == UNIT_UNKNOWN){
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }
//        if (barcodeData != null){
//           if (barcodeData.GTIN == null){
//               return false;
//            }else if (barcodeData.Weight == 0) {
//                return false;
//            }else if (barcodeData.Weight_Unit == 2){
//                return false;
//            }else if (barcodeData.PackingDate == null){
//                return false;
//            }else if (barcodeData.Serial == null){
//                return false;
//            }else {
//                return true;
//            }
//        }else {
//            return false;
//        }
    }

    private void ConversionBarcodeData(Map<String, String> data){
        barcodeData = new BarcodeData();
        for ( String key : data.keySet() ) {
            if (key.equals("01")){
                barcodeData.GTIN = data.get("01");
            }else if (key.equals("21")){
                barcodeData.Serial = data.get("21");
            }else if (key.equals("11")){
                barcodeData.ProducerDate = ConvertPackingDate(data.get("11"));
            }else if (key.equals("13")) {
                barcodeData.PackingDate = ConvertPackingDate(data.get("13"));
            }else if(key.length() == 4 && (key.substring(0,3).equals("310") || key.substring(0,3).equals("320"))){
                GetWeight(key,data.get(key));
            }
        }
        if (!BarcodeDataCheck()){
            barcodeData = null;
        }
    }

    private Date ConvertPackingDate(String data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
            return sdf.parse(data);
        }catch (Exception ex){
            return null;
        }
    }

    private void GetWeight(String key, String data){
        try {
            if (key.substring(0, 3).equals("310")) {
                barcodeData.Weight_Unit = UNIT_KG;
                barcodeData.Weight = ConvertWeight(Integer.parseInt(key.substring(3, 4)), data);
            } else if (key.substring(0, 3).equals("320")) {
                barcodeData.Weight_Unit = UNIT_LB;
                barcodeData.Weight = ConvertWeight(Integer.parseInt(key.substring(3, 4)), data);
            }
        }catch (Exception ex){
            barcodeData.Weight_Unit = UNIT_UNKNOWN;
            barcodeData.Weight = 0;
        }
    }

    private double ConvertWeight(int decimalPlaces,String data) {
        double weight;
        try {
            weight = Double.parseDouble(data);
            BigDecimal unscaled = new BigDecimal(weight);
            BigDecimal scaled = unscaled.scaleByPowerOfTen(-decimalPlaces);
            weight = scaled.doubleValue();
            return weight;
        } catch (Exception ex) {
            weight = 0;
            return weight;
        }
    }
}

