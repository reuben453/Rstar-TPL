/*Last update 6-Nov-97 */
/* Updated by Josephine Wong 23 November 1997
   Changed to use User Specified values rather than Constants defined.
*/
package userinterface;
import java.awt.*;

public class rectangle
{
    public int id;
    public int UX,UY,LX,LY;
    public int bounds[] = new int[2*Constants.DIMENSION];
    public short Prop;

    public rectangle(int i)
    {
        this.id=i;
//        this.LX=(int) ((Constants.MAXCOORD-1)*Math.random());
//        this.LY=(int) ((Constants.MAXCOORD-1)*Math.random());
        for(int j = 0; j < Constants.DIMENSION; j++)
        {
        	this.bounds[2*j]   = (int) ((UserInterface.getMAXCOORD()-1)*Math.random());
        	//this.M[2*j+1] = (int) ((UserInterface.getMAXCOORD()-1)*Math.random());
        	//this.LX=(int) ((UserInterface.getMAXCOORD()-1)*Math.random());
        	//this.LY=(int) ((UserInterface.getMAXCOORD()-1)*Math.random());
        }
        for(int j = 0; j < Constants.DIMENSION; j++)
        {
	        do
	        {
	        	
	        		this.bounds[2*j+1] = (int) (Constants.min (
	//                  Constants.MAXCOORD-this.LX,
	//                  Constants.MAXWIDTH
	                  UserInterface.getMAXCOORD()-this.bounds[2*j],
	                  UserInterface.getMAXWIDTH()
	              )*Math.random()+this.bounds[2*j]);
	            /*this.UX=(int) (Constants.min (
	//                                Constants.MAXCOORD-this.LX,
	//                                Constants.MAXWIDTH
	                                UserInterface.getMAXCOORD()-this.LX,
	                                UserInterface.getMAXWIDTH()
	                            )*Math.random()+this.LX);*/
	        } while (bounds[2*j+1] == bounds[2*j]);
	       // } while (UX == LX);
        }
                           
        /*do
        {
            this.UY=(int) (Constants.min ( 
//                                Constants.MAXCOORD-this.LY,
//                                Constants.MAXWIDTH
                                UserInterface.getMAXCOORD()-this.LY,
                                UserInterface.getMAXWIDTH()
                            )*Math.random()+this.LY);
        } while (UX == LX);*/
        
        for(int j = 0; j < Constants.DIMENSION; j++)
        	bounds[2*j+1] = bounds[2*j];
        
        this.Prop=(short) (5*Math.random() + 1);
        LX = bounds[0];
        UX = bounds[1];
        LY = bounds[2];
        UY = bounds[3];
    }

    public void print()
    {
    	for(int i = 0; i < 2*Constants.DIMENSION; i++)
    		System.out.println(this.bounds[i]+" , ");
    	//System.out.println(this.LX+" , "+this.LY);
    	//System.out.println(this.UX+" , "+this.UY);
    }
    
    public Rectangle toRectangle()
    {
    	return new Rectangle(bounds[0], bounds[1], bounds[2]-bounds[0], bounds[3]-bounds[1]);
        //return new Rectangle(LX, LY, UX-LX, UY-LY);
    }
}
