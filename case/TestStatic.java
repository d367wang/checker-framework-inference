import fenum.qual.Fenum;
import fenum.qual.FenumA;
import fenum.qual.FenumB;


public class TestStatic {

  public static final @FenumA int ACONST1 = 1;


  public static final @Fenum("A") int ACONST2 = 2;

  public static final @FenumB String BCONST1 = "FenumB";

  public static final @Fenum("B") int BCONST2 = 5; 
  
}

