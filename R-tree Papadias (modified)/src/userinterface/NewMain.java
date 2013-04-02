/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;

import java.awt.*;


public class NewMain extends Canvas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	//System.out.println("***********ONE***********************");
   //   Graphics g= getGraphics();
     TreeCreation tc = new TreeCreation("ab.rtr",20, 2,256,128);
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
		 float mindist = -1, temp = 0;
		 int min_i = -1;
		 
		 //float temp1 = temp_node.entries[0].son_ptr.data[0].data[1];
		 //temp = Constants.MINDIST(p, temp_node.entries[0].get_son().get_mbr());
		 //System.out.println("THHHHHHHHHHHHHH"+temp);
		 
     while(true)
     {
    	 mindist = -1;
    	 min_i = -1;
    	 if(temp_node != null)                           //temp_node is a RTDirNode
    	 {
    		 for(int i = 0; i < temp_node.num_entries; i++)
    		 {
    			 if((temp=Constants.MINDIST(p, temp_node.entries[i].bounces)) < mindist || mindist == -1)
    			 {
    				 min_i = i;
    				 mindist = temp;
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
    	 else                       //temp_node is null then temp_node1 is not null, temp_node1 is a RTDataNode
    	 {
    		 //System.out.println("ELSE");
    		 for(int i = 0; i < temp_node1.num_entries; i++)
    			 if((temp = (float) (Math.pow(p.data[0]-temp_node1.data[i].data[0], 2)+Math.pow(p.data[1]-temp_node1.data[i].data[2], 2))) < mindist || mindist == -1)
    			 {	 
    				 mindist = temp;
    				 min_i = i;
    			 }
    		 if(min_i == -1)
    			 System.out.println("ERROR!!!!!");
    		 else
    			 System.out.println("RESULT=  "+temp_node1.data[min_i].data[0]+" "+temp_node1.data[min_i].data[2]+"\nDistance="+mindist);
    		 break;
    	 }
     }
		// TPL_algorithm.Reverse_nearest_neighbour(tc.rt, p);
      
      
      
              /* System.out.print(tc.f.framedArea.area.currentRect.y+" ");
      System.out.print(tc.f.framedArea.area.currentRect.height+" ");
         System.out.print(tc.f.framedArea.area.currentRect.width);*/
        
        
    }
   
   
}
