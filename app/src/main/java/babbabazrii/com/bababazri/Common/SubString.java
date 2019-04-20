package babbabazrii.com.bababazri.Common;

public class SubString {
    public String getLastnCharacters(String inputString, int subStringLength){
        int length = inputString.length();
        if(length <= subStringLength){
            return inputString;
        }
        int startIndex = length-subStringLength;
        return inputString.substring(startIndex);
    }
    public String removeLastnCharacters(String inputString, int subStringLength){
        int length = inputString.length();
        if(length <= subStringLength){
            return inputString;
        }
        int endIndex = (length-subStringLength);
        return inputString.substring(0,endIndex);
    }
}
