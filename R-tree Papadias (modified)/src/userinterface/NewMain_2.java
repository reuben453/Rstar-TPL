/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;

import java.awt.*;
import java.applet.Applet;
//import java.

/**
 *
 * @author Abhinav
 */
public class NewMain_2 extends Canvas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	System.out.println("***********ONE***********************");
   //   Graphics g= getGraphics();
     TreeCreation tc = new TreeCreation("ab.rtr",200, 2,256,128);
     float mb[] = tc.rt.root_ptr.get_mbr();
      //System.out.print(mb[0]+" "+mb[1]+" "+mb[2]+" "+mb[3]+" ");
      
      RTDirNode temp_node = null;
      RTDataNode temp_node1 = null;
      if(tc.rt.root_is_data == false)
    	  temp_node = (RTDirNode) tc.rt.root_ptr;
      else
    	  temp_node1 = (RTDataNode) tc.rt.root_ptr;
      
      PPoint p = new PPoint();
		 p.data[0] = 0;
		 p.data[1] = 0;
		 float min = 100000, temp = 0;
		 int min_i = -1;
		 
		 //float temp1 = temp_node.entries[0].son_ptr.data[0].data[1];
		 //temp = Constants.MINDIST(p, temp_node.entries[0].get_son().get_mbr());
		 //System.out.println("THHHHHHHHHHHHHH"+temp);
		 
     while(true)
     {
    	 min = 100000;
    	 min_i = -1;
    	 if(temp_node != null)
    	 {
    		 for(int i = 0; i < temp_node.num_entries; i++)
    		 {
    			 if((temp=Constants.MINDIST(p, temp_node.entries[i].bounces)) < min)
    			 {
    				 min_i = i;
    				 min = temp;
    			 }
    		 }
    		 if(min_i == -1)
    			 System.out.println("ERROR!!!!!");
    		 else
    		 {
    			 if(temp_node.entries[min_i].son_is_data == false)
    				 temp_node = (RTDirNode) temp_node.entries[min_i].get_son();
    			 else
    			 {
    				temp_node1 = (RTDataNode) temp_node.entries[min_i].get_son();
    				temp_node = null;
    			 }
    			 
    		 }
    		 
    	 }
    	 else
    	 {
    		 System.out.println("ELSE");
    		 for(int i = 0; i < temp_node1.num_entries; i++)
    			 if((temp = (float) (Math.pow(p.data[0]-temp_node1.data[i].data[0], 2)+Math.pow(p.data[1]-temp_node1.data[i].data[2], 2))) < min)
    			 {	 
    				 min = temp;
    				 min_i = i;
    			 }
    		 if(min_i == -1)
    			 System.out.println("ERROR!!!!!");
    		 else
    			 System.out.println("RESULT=  "+temp_node1.data[min_i].data[0]+" "+temp_node1.data[min_i].data[2]);
    		 break;
    	 }
     }
      
      
      
              /* System.out.print(tc.f.framedArea.area.currentRect.y+" ");
      System.out.print(tc.f.framedArea.area.currentRect.height+" ");
         System.out.print(tc.f.framedArea.area.currentRect.width);*/
        
        
    }
   
   
}
