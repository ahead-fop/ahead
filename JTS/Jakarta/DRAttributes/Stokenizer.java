package Jakarta.DRAttributes;
import java.util.*;

public class Stokenizer extends StringTokenizer {
   public Stokenizer(String x) {
      super(x,"[], ",true);
   }

   public String nextNonBlankToken() throws ParseErrorException {
      String result;
      try {
         while (true) {
            result = nextToken();
            if (result.compareTo(" ") != 0) return result;
         }
      }
      catch (NoSuchElementException e) {
         throw new ParseErrorException("Unexpected End-of-Input");
      }
   }

   public void nextTokenIs(String x) throws ParseErrorException {
      String next;
      try {
         next = nextNonBlankToken();
         if (next.compareTo(x)==0)
            return;
         throw new ParseErrorException("expecting \""+x+"\" instead of \""
                   +next +"\"");
      }
      catch (NoSuchElementException e) {
         throw new ParseErrorException("Unexpected End-of-Input");
      }
   }

   public void nextTokenIs(String x, String y) throws ParseErrorException {
      String next;
      try {
         next = nextNonBlankToken();
         if (next.compareTo(x)==0 | next.compareTo(y)==0)
            return;
         throw new ParseErrorException("expecting \""+x + "\" or \"" + y +"\" instead of \""
                   +next +"\"");
      }
      catch (NoSuchElementException e) {
         throw new ParseErrorException("Unexpected End-of-Input");
      }
   }

   public void nextTokenIsLB() throws ParseErrorException {
      nextTokenIs("[");
   }

   public void nextTokenIsRB() throws ParseErrorException {
      nextTokenIs("]");
   }

   public void nextTokenIsComma() throws ParseErrorException {
      nextTokenIs(",");
   }

   public String nextTokenPastComma() throws ParseErrorException{
      nextTokenIsComma();
      return nextNonBlankToken();
   }
} 

