package com.biotech.framework.barcode

import java.util.*

class GS128 {

    companion object {

    }
    
    private val aiiDict = HashMap<String, AII>()
    private val aiis: Array<String>? = null
    private var minLengthOfAI = 99
    private var maxLengthOfAI = 1
    private val groutSeperator = 29.toChar()
    private val startCode = "]C1"
    private val hasCheckSum = false
    private var barcodeIndex = 0

    enum class DataType {
        Numeric,
        Alphanumeric
    }

//    enum class WeightUnit {
//
//        UNKNOWN,
//        KG,
//        LB
//    }
    enum class WeightUnit {
        LB,
        KG,
        UNKNOWN
    }

    /// <summary>
    /// Information Class for an Application Identifier (AI)
    /// </summary>
    data class AII (

        var AI: String,
        var Description: String,
        var LengthOfAI: Int,
        var DataDescription: DataType,
        var LengthOfData: Int,
        var FNC1: Boolean
    )

    init {
        add("00", "運輸容器序號", 2, DataType.Numeric, 18, false)
        add("01", "商品交易品項 GTIN-14", 2, DataType.Numeric, 14, false)
        add("02", "最大物流包裝 GTIN-14", 2, DataType.Numeric, 14, false)
        add("10", "批號", 2, DataType.Alphanumeric, 20, true)
        add("11", "生產日 YYMMDD", 2, DataType.Numeric, 6, false)
        add("12", "DueDate_JJMMDD", 2, DataType.Numeric, 6, false)
        add("13", "包裝日 YYMMDD", 2, DataType.Numeric, 6, false)
        add("15", "MinimumDurabilityDate_JJMMDD", 2, DataType.Numeric, 6, false)
        add("17", "有效日 YYMMDD", 2, DataType.Numeric, 6, false)
        add("20", "ProductModel", 2, DataType.Numeric, 2, false)
        add("21", "序號", 2, DataType.Alphanumeric, 20, true)
        add("22", "HIBCCNumber", 2, DataType.Alphanumeric, 29, false)
        add("240", "PruductIdentificationOfProducer", 3, DataType.Alphanumeric, 30, true)
        add("241", "CustomerPartsNumber", 3, DataType.Alphanumeric, 30, true)
        add("250", "SerialNumberOfAIntegratedModule", 3, DataType.Alphanumeric, 30, true)
        add("251", "ReferenceToTheBasisUnit", 3, DataType.Alphanumeric, 30, true)
        add("252", "GlobalIdentifierSerialisedForTrade", 3, DataType.Numeric, 2, false)
        add("30", "AmountInParts", 2, DataType.Numeric, 8, true)
        add("310d", "淨重(公斤)", 4, DataType.Numeric, 6, false)
        add("311d", "長度(公尺)", 4, DataType.Numeric, 6, false)
        add("312d", "寬度(公尺)", 4, DataType.Numeric, 6, false)
        add("313d", "高度(公尺)", 4, DataType.Numeric, 6, false)
        add("314d", "表面績(立方公尺)", 4, DataType.Numeric, 6, false)
        add("315d", "NetVolume_Liters", 4, DataType.Numeric, 6, false)
        add("316d", "NetVolume_CubicMeters", 4, DataType.Numeric, 6, false)
        add("320d", "淨重(磅)", 4, DataType.Numeric, 6, false)
        add("321d", "Length_Inches", 4, DataType.Numeric, 6, false)
        add("322d", "Length_Feet", 4, DataType.Numeric, 6, false)
        add("323d", "Length_Yards", 4, DataType.Numeric, 6, false)
        add("324d", "Width_Inches", 4, DataType.Numeric, 6, false)
        add("325d", "Width_Feed", 4, DataType.Numeric, 6, false)
        add("326d", "Width_Yards", 4, DataType.Numeric, 6, false)
        add("327d", "Heigth_Inches", 4, DataType.Numeric, 6, false)
        add("328d", "Heigth_Feed", 4, DataType.Numeric, 6, false)
        add("329d", "Heigth_Yards", 4, DataType.Numeric, 6, false)
        add("330d", "GrossWeight_Kilogram", 4, DataType.Numeric, 6, false)
        add("331d", "Length_Meter", 4, DataType.Numeric, 6, false)
        add("332d", "Width_Meter", 4, DataType.Numeric, 6, false)
        add("333d", "Heigth_Meter", 4, DataType.Numeric, 6, false)
        add("334d", "Surface_SquareMeter", 4, DataType.Numeric, 6, false)
        add("335d", "GrossVolume_Liters", 4, DataType.Numeric, 6, false)
        add("336d", "GrossVolume_CubicMeters", 4, DataType.Numeric, 6, false)
        add("337d", "KilogramPerSquareMeter", 4, DataType.Numeric, 6, false)
        add("340d", "GrossWeight_Pounds", 4, DataType.Numeric, 6, false)
        add("341d", "Length_Inches", 4, DataType.Numeric, 6, false)
        add("342d", "Length_Feet", 4, DataType.Numeric, 6, false)
        add("343d", "Length_Yards", 4, DataType.Numeric, 6, false)
        add("344d", "Width_Inches", 4, DataType.Numeric, 6, false)
        add("345d", "Width_Feed", 4, DataType.Numeric, 6, false)
        add("346d", "Width_Yards", 4, DataType.Numeric, 6, false)
        add("347d", "Heigth_Inches", 4, DataType.Numeric, 6, false)
        add("348d", "Heigth_Feed", 4, DataType.Numeric, 6, false)
        add("349d", "Heigth_Yards", 4, DataType.Numeric, 6, false)
        add("350d", "Surface_SquareInches", 4, DataType.Numeric, 6, false)
        add("351d", "Surface_SquareFeet", 4, DataType.Numeric, 6, false)
        add("352d", "Surface_SquareYards", 4, DataType.Numeric, 6, false)
        add("353d", "Surface_SquareInches", 4, DataType.Numeric, 6, false)
        add("354d", "Surface_SquareFeed", 4, DataType.Numeric, 6, false)
        add("355d", "Surface_SquareYards", 4, DataType.Numeric, 6, false)
        add("356d", "NetWeight_TroyOunces", 4, DataType.Numeric, 6, false)
        add("357d", "NetVolume_Ounces", 4, DataType.Numeric, 6, false)
        add("360d", "NetVolume_Quarts", 4, DataType.Numeric, 6, false)
        add("361d", "NetVolume_Gallonen", 4, DataType.Numeric, 6, false)
        add("362d", "GrossVolume_Quarts", 4, DataType.Numeric, 6, false)
        add("363d", "GrossVolume_Gallonen", 4, DataType.Numeric, 6, false)
        add("364d", "NetVolume_CubicInches", 4, DataType.Numeric, 6, false)
        add("365d", "NetVolume_CubicFeet", 4, DataType.Numeric, 6, false)
        add("366d", "NetVolume_CubicYards", 4, DataType.Numeric, 6, false)
        add("367d", "GrossVolume_CubicInches", 4, DataType.Numeric, 6, false)
        add("368d", "GrossVolume_CubicFeet", 4, DataType.Numeric, 6, false)
        add("369d", "GrossVolume_CubicYards", 4, DataType.Numeric, 6, false)
        add("37", "QuantityInParts", 2, DataType.Numeric, 8, true)
        add("390d", "AmountDue_DefinedValutaBand", 4, DataType.Numeric, 15, true)
        add("391d", "AmountDue_WithISOValutaCode", 4, DataType.Numeric, 18, true)
        add("392d", "BePayingAmount_DefinedValutaBand", 4, DataType.Numeric, 15, true)
        add("393d", "BePayingAmount_WithISOValutaCode", 4, DataType.Numeric, 18, true)
        add("400", "JobNumberOfGoodsRecipient", 3, DataType.Alphanumeric, 30, true)
        add("401", "ShippingNumber", 3, DataType.Alphanumeric, 30, true)
        add("402", "DeliveryNumber", 3, DataType.Numeric, 17, false)
        add("403", "RoutingCode", 3, DataType.Alphanumeric, 30, true)
        add("410", "EAN_UCC_GlobalLocationNumber(GLN)_GoodsRecipient", 3, DataType.Numeric, 13, false)
        add("411", "EAN_UCC_GlobalLocationNumber(GLN)_InvoiceRecipient", 3, DataType.Numeric, 13, false)
        add("412", "EAN_UCC_GlobalLocationNumber(GLN)_Distributor", 3, DataType.Numeric, 13, false)
        add("413", "EAN_UCC_GlobalLocationNumber(GLN)_FinalRecipient", 3, DataType.Numeric, 13, false)
        add("414", "EAN_UCC_GlobalLocationNumber(GLN)_PhysicalLocation", 3, DataType.Numeric, 13, false)
        add("415", "EAN_UCC_GlobalLocationNumber(GLN)_ToBilligParticipant", 3, DataType.Numeric, 13, false)
        add("420", "ZipCodeOfRecipient_withoutCountryCode", 3, DataType.Alphanumeric, 20, true)
        add("421", "ZipCodeOfRecipient_withCountryCode", 3, DataType.Alphanumeric, 12, true)
        add("422", "BasisCountryOfTheWares_ISO3166Format", 3, DataType.Numeric, 3, false)
        add("7001", "Nato Stock Number", 4, DataType.Numeric, 13, false)
        add("7007", "收成日 YYMMDD", 4, DataType.Numeric, 6, false)
        add("8001", "RolesProducts", 4, DataType.Numeric, 14, false)
        add("8002", "SerialNumberForMobilePhones", 4, DataType.Alphanumeric, 20, true)
        add("8003", "GlobalReturnableAssetIdentifier", 4, DataType.Alphanumeric, 34, true)
        add("8004", "GlobalIndividualAssetIdentifier", 4, DataType.Numeric, 30, true)
        add("8005", "SalesPricePerUnit", 4, DataType.Numeric, 6, false)
        add("8006", "IdentifikationOfAProductComponent", 4, DataType.Numeric, 18, false)
        add("8007", "IBAN", 4, DataType.Alphanumeric, 30, true)
        add("8008", "DataAndTimeOfManufacturing", 4, DataType.Numeric, 12, true)
        add("8018", "GlobalServiceRelationNumber", 4, DataType.Numeric, 18, false)
        add("8020", "NumberBillCoverNumber", 4, DataType.Alphanumeric, 25, false)
        add("8100", "CouponExtendedCode_NSC_offerCcode", 4, DataType.Numeric, 10, false)
        add("8101", "CouponExtendedCode_NSC_offerCcode_EndOfOfferCode", 4, DataType.Numeric, 14, false)
        add("8102", "CouponExtendedCode_NSC", 4, DataType.Numeric, 6, false)
        add("90", "InformationForBilateralCoordinatedApplications", 2, DataType.Alphanumeric, 30, true)
        add("91", "公司指定", 2, DataType.Alphanumeric, 30, true);
        add("92", "公司指定", 2, DataType.Alphanumeric, 30, true);
        add("93", "公司指定", 2, DataType.Alphanumeric, 30, true);
        add("94", "公司指定", 2, DataType.Alphanumeric, 30, true);
        add("95", "公司指定", 2, DataType.Alphanumeric, 30, true);
        add("96", "公司指定", 2, DataType.Alphanumeric, 30, true);
        add("97", "公司指定", 2, DataType.Alphanumeric, 30, true);
        add("98", "公司指定", 2, DataType.Alphanumeric, 30, true);
        add("99", "公司指定", 2, DataType.Alphanumeric, 30, true);
        //        aiis = aiiDict.Keys.ToArray();
        //        minLengthOfAI = aiiDict.Values.Min(el => el.LengthOfAI);
        //        maxLengthOfAI = aiiDict.Values.Max(el => el.LengthOfAI);
        //        minLengthOfAI = 2;
        //        maxLengthOfAI = 4;
        for (aii in aiiDict.values) {
            if (aii.LengthOfAI < minLengthOfAI) {
                minLengthOfAI = aii.LengthOfAI
            }
        }

        for (aii in aiiDict.values) {
            if (aii.LengthOfAI > maxLengthOfAI) {
                maxLengthOfAI = aii.LengthOfAI
            }
        }
    }

    fun getDescription(ai : String) : AII?
    {
        val alternateKey = ai.removeRange(ai.length - 1, ai.length) + "d"
        return when {
            ai.isEmpty() -> null
            aiiDict.containsKey(ai) -> aiiDict[ai]
            aiiDict.containsKey(alternateKey) -> aiiDict[alternateKey]
            else -> null
        }
    }





    fun Parse(raw: String): Map<String, String> {
        var data = raw
        // cut off the EAN128 start code
        if (data.startsWith(getStartCode()))
            data = data.substring(getStartCode().length)
        //            data = data.Substring(GetEAN128StartCode().length());
        // cut off the check sum
        if (hasCheckSum())
            data = String(data.toByteArray(), 0, data.length - 2)
        //            data = data.Substring(0, data.Length - 2);

        val result = HashMap<String, String>()
        barcodeIndex = 0
        // walkk through the EAN128 code
        while (barcodeIndex < data.length) {
            // try to get the AI at the current position
            val ai = getAI(data)
                ?: //                if(throwException)
                //                    throw new InvalidOperationException("AI not found");
                return result
            // get the data to the current AI
            val code = getCode(data, ai)
            //            result[ai] = code;
            result[ai.AI] = code
        }

        return result
    }

    private fun getAI(data: String, usePlaceHolder: Boolean = false): AII? {
        var result: AII? = null

        // Step through the different lenghts of the AIs
        for (i in minLengthOfAI..maxLengthOfAI) {
            if (barcodeIndex + i > data.length) {
                break
            }
            // get the AI sub string
            // String ai = data.Substring(index, i);
            var ai = String(data.toByteArray(), barcodeIndex, i)
            if (usePlaceHolder) {
                ai = ai.substring(0, ai.length - 1) + "d"
            }
            //                ai = ai.Remove(ai.Length - 1) + "d";
            // try to get the ai from the dictionary
            //            if (aiiDict.TryGetValue(ai, out result))
            result = aiiDict[ai]
            if (result != null) {
                if (ai.substring(ai.length - 1, ai.length) == "d") {
                    result.AI = String(data.toByteArray(), barcodeIndex, i)
                }
                // Shift the index to the next
                barcodeIndex += i
                return result
            }
            // if no AI found, try it with the next length
        }
        // if no AI found here, than try it with placeholders. Assumed that is the first sep where usePlaceHolder is false
        if (!usePlaceHolder)
            result = getAI(data, true)
        return result
    }

    private fun getCode(data: String, ai: AII): String {
        // get the max lenght to read.
        var lenghtToRead = Math.min(ai.LengthOfData, data.length - barcodeIndex)
        // get the data of the current AI
        //        String result = data.Substring(index, lenghtToRead);
        var result = String(data.toByteArray(), barcodeIndex, lenghtToRead)

        // check if the AI support a group seperator
        if (ai.FNC1) {
            // try to find the index of the group seperator
            val indexOfGroupTermination = result.indexOf(getGroutSeperator())
            if (indexOfGroupTermination >= 0)
                lenghtToRead = indexOfGroupTermination + 1
            // get the data of the current AI till the gorup seperator
            //            result = data.Substring(index, lenghtToRead);
            result = String(data.toByteArray(), barcodeIndex, lenghtToRead)
        }

        // Shift the index to the next
        barcodeIndex += lenghtToRead
        return result
    }



    private fun add(
        AI: String,
        Description: String,
        LengthOfAI: Int,
        DataDescription: DataType,
        LengthOfData: Int,
        FNC1: Boolean
    ) {
        aiiDict[AI] = AII(AI, Description, LengthOfAI, DataDescription, LengthOfData, FNC1)
    }

    private fun getStartCode() = startCode

    private fun getGroutSeperator() = groutSeperator

    private fun hasCheckSum() = hasCheckSum
}