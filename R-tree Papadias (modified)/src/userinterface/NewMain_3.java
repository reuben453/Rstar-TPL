/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;

import java.awt.*;


public class NewMain_3 extends Canvas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	//System.out.println("***********ONE***********************");
   //   Graphics g= getGraphics();
     TreeCreation tc = new TreeCreation("ab.rtr",200, 2,256,128);
     //float mb[] = tc.rt.root_ptr.get_mbr();
      //System.out.print(mb[0]+" "+mb[1]+" "+mb[2]+" "+mb[3]+" ");
      
      RTDirNode temp_node = null;
      RTDataNode temp_node1 = null;
      if(tc.rt.root_is_data == false)
    	  temp_node = (RTDirNode) tc.rt.root_ptr;
      else
    	  temp_node1 = (RTDataNode) tc.rt.root_ptr;
      
      PPoint p = new PPoint();
		 p.data[0] = 128;
		 p.data[1] = 100;
                 TPL_algorithm a = new TPL_algorithm() ;
                 a.call(tc.rt, p);          
         tc.display();	
        
        
    }
   
   
}
