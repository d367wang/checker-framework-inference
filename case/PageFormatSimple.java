import fenum.qual.Fenum;

public class PageFormatSimple implements Cloneable
{
  public static final @Fenum("orientation") int LANDSCAPE = 0;
  public static final @Fenum("orientation") int PORTRAIT = 1;
  public static final @Fenum("orientation") int REVERSE_LANDSCAPE = 2;

  private int orientation;
	
  public int getOrientation() {
    return this.orientation;
  }

  public void setOrientation(int orientation){
    this.orientation = orientation;
  }
  
  
  public void foo() {
	  switch (getOrientation()) {
		  case LANDSCAPE:break;
      case PORTRAIT:break;
      case REVERSE_LANDSCAPE:break;
		  default:
    }
  }

}


