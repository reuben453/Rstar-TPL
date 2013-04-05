/* Modified by Josephine Wong 24 November 1997 to incorporate User Interface.
   controller is now cast to type TreeCreation, and a rectangle is only drawn
   if its level is not less than the selected display level.
*/
package userinterface;

import java.awt.*;
import java.util.Iterator;

class RectArea extends Canvas {
	SortedLinList queryres=null;
    Rectangle currentRect=null;
    Object controller;
    String outp = "";
    
    // for displaying queries area
    boolean displayed = false;
    float[] range = null;
    PPoint p = null;
    float radius1 = 0.0f;
    float radius2 = 0.0f;

    public RectArea(Object controller) {
        super();
        this.controller = controller;
        //currentRect = new Rectangle(22, 32, 24, 43);
    }

    public void drawNode(RTNode node, Graphics g)
    {
    	if (((TreeCreation)controller).displaylevel == 200) return; // display only query results
        Dimension d = getSize();
        //System.out.println("Now it begins\n\n\n\n\n\n");
        Color prev = g.getColor();
        switch(node.level)
        {
            case 0:
                g.setColor(Color.black);
                break;
            case 1:
                g.setColor(Color.blue);
                break;
            case 2:
                g.setColor(Color.green);
                break;
            case 3:
                g.setColor(Color.red);
                break;
            case 4:
                g.setColor(Color.magenta);
                break;
            case 5:
                g.setColor(Color.lightGray);
                break;
        }
       
        for (int i=0; i<node.get_num(); i++)
        {
            if (node instanceof RTDataNode)
            {
                RTDataNode p = null;
                System.out.println("No. of candidate set items "+TPL_algorithm.cand_set.size());
                if(TPL_algorithm.cand_set.size()>=1 && Constants.RNN_Check==false)
                {
                    Iterator <RTDataNode> i1 = TPL_algorithm.cand_set.iterator();
                    p=i1.next();
                g.setColor(Color.red);
                 g.fillOval((int)(p.data[0].data[0]), (int)(p.data[0].data[2]), 5, 5);
                 
                 Constants.RNN_Check=true;
                 /*if(TPL_algorithm.cand_set.size()==1)
                     System.exit(1);
                 else
                     return;*/
                }		
               
                RTDataNode datanode = (RTDataNode)node;

                Rectangle r = new Rectangle((int)datanode.data[i].get_mbr()[0],
                                            (int)datanode.data[i].get_mbr()[2],
                                            (int)datanode.data[i].get_mbr()[1] - (int)datanode.data[i].get_mbr()[0],
                                            (int)datanode.data[i].get_mbr()[3] - (int)datanode.data[i].get_mbr()[2]);
                
                
                Rectangle r1 = new Rectangle((int)datanode.data[i].get_mbr()[0],
                        (int)datanode.data[i].get_mbr()[2],
                        (int)datanode.data[i].get_mbr()[1] - (int)datanode.data[i].get_mbr()[0],
                        (int)datanode.data[i].get_mbr()[3] - (int)datanode.data[i].get_mbr()[2]);
                
                
                
                
                
                
                outp+="Rect " + i +": " + r.x + " " + (r.x + r.width) + " "+ r.y + " "+ (r.y + r.height) + " "+"\n";
                Rectangle box = getDrawableRect(r, d);
                if ((((TreeCreation)controller).displaylevel==199) ||
                	   (node.level == ((TreeCreation)controller).displaylevel))
                {		g.drawRect(box.x, box.y, box.width - 1, box.height - 1);
                //System.out.println(box.x + " " + box.y+ " " + (box.width - 1)+ " " + (box.height - 1));
                     //System.out.println((box.width - 1)+" "+(box.height - 1));
                     if(box.width - 1<=0)
                    	 g.drawOval(box.x, box.y, 5, 5);
                }
            }
            else
            {
                RTDirNode dirnode = (RTDirNode)node;

                Node n = (Node)dirnode.entries[i].get_son();

                float[] mbr = n.get_res_mbr();
                float[] mbr1 = n.get_mbr();
                Color temp = g.getColor();
                if(!(n.get_mbr()[0] == n.get_res_mbr()[0] && n.get_mbr()[1] == n.get_res_mbr()[1] && n.get_mbr()[2] == n.get_res_mbr()[2] && n.get_mbr()[3] == n.get_res_mbr()[3]))
                	g.setColor(Color.ORANGE);

                Rectangle r = new Rectangle((int)mbr[0],
                                            (int)mbr[2],
                                            (int)mbr[1] - (int)mbr[0],
                                            (int)mbr[3] - (int)mbr[2]);
                
              /*  Rectangle r1 = new Rectangle((int)mbr1[0],
                        (int)mbr1[2],
                        (int)mbr1[1] - (int)mbr1[0],
                        (int)mbr1[3] - (int)mbr1[2]);*/

                //outp+="Dirnode level " + node.level + " entry " + i + ": " + r.x + " " + (r.x + r.width) + " "+ r.y + " "+ (r.y + r.height) + " " + "\n";
                //Rectangle box = getDrawableRect(r, d);
                Rectangle box1 = getDrawableRect(r, d);
                if(mbr[0]!=-1) //&& mbr[1]!=-1 && mbr[2]!=-1 && mbr[3]!=-1)
                drawNode(dirnode.entries[i].get_son(), g);

                if ((((TreeCreation)controller).displaylevel==199) ||
                	   (node.level == ((TreeCreation)controller).displaylevel))
                {
                  //  g.drawRect(box.x, box.y, box.width - 1, box.height - 1);
                    //System.out.println((box.width - 1)+" "+(box.height - 1));
                 //    if(box.width - 1<=0)
               //     g.drawOval(box.x, box.y, 5, 5);
                     g.setColor(temp);
                     g.drawRect(box1.x, box1.y, box1.width - 1, box1.height - 1);
                   //  System.out.println(box1.x +" "+ box1.y +" "+ box1.width +" "+ (box1.height - 1)+" ");
                }
            }
        }
        
        g.setColor(prev);
    }

    public void drawRange(float[] mbr)
    {
        this.p = null;
        this.range = mbr;
        repaint();
    }
    
    public void drawRing(PPoint p, float r1, float r2)
    {
        this.range = null;
        this.p = p;
        this.radius1 = r1;
        this.radius2 = r2;
        repaint();
    }
    
    public void paint(Graphics g) {
//        RTree rt = ((Test)controller).rt;

        RTree rt = ((TreeCreation)controller).rt;
        RTNode node = rt.root_ptr;
        outp = "";
        float mb[] = null;
        float mb1[] = null;
        if(rt.root_is_data == true)
        {
        	if(rt.root_ptr.res_mbr != null)
        		mb1 = ((RTDataNode) rt.root_ptr).get_res_mbr();
        	mb = ((RTDataNode) rt.root_ptr).get_mbr();
        }
        else
        {
        	if(rt.root_ptr.res_mbr != null)
        		mb1 = ((RTDirNode) rt.root_ptr).get_res_mbr();
        	mb = ((RTDirNode) rt.root_ptr).get_mbr();
        }
         //THIS IS ROOT :D
      //System.out.println("Root" + mb[0]+" "+mb[1]+" "+mb[2]+" "+mb[3]+" ");
      
        
         //g.drawre
        /*
         * Draw the residual root mbr
         */
        g.setColor(Color.ORANGE);
        if(mb1 != null && mb1[1] != -1)
        	g.drawRect((int)mb1[0],(int)mb1[2],((int)mb1[1]-(int)mb1[0]),((int)mb1[3]-(int)mb1[2]));
        
      g.setColor(Color.CYAN);
      //System.out.println("This"+(int)mb[0]+" "+(int)mb[2]+" "+(int)(mb[1]-mb[0])+" "+(int)(mb[3]-mb[2]));
        g.drawRect((int)mb[0],(int)mb[2],((int)mb[1]-(int)mb[0]),((int)mb[3]-(int)mb[2]));
        
        
        drawNode(node, g);
        
        if ((((TreeCreation)controller).displaylevel == 200) && (queryres!=null))
        {
            Dimension d = getSize();
            
	        for (Object obj = queryres.get_first(); obj != null; obj = queryres.get_next())
	        {
	        	float mbr[] = ((Data)obj).data;
	        	Rectangle r = new Rectangle((int)mbr[0], (int)mbr[2], (int)mbr[1] - (int)mbr[0], (int)mbr[3] - (int)mbr[2]);
            	Rectangle box = getDrawableRect(r, d);
                g.setColor(Color.BLACK);
            	g.drawRect(box.x, box.y, box.width - 1, box.height - 1);
               
            }
        }
        
        if (range!=null)
        {
            Dimension d = getSize();
            
            Color prev = g.getColor();
            g.setColor(Color.red);
            
            Rectangle r = new Rectangle((int)range[0], (int)range[2], (int)range[1] - (int)range[0], (int)range[3] - (int)range[2]);
            Rectangle box = getDrawableRect(r, d);
            g.drawRect(box.x, box.y, box.width - 1, box.height - 1);
                   
            g.setColor(prev);
        }
        
        if (p!=null)
        {
            Color prev = g.getColor();
            g.setColor(Color.red);
            g.drawOval((int)p.data[0]*3-(int)radius1*3, (int)p.data[1]*3-(int)radius1*3, (int)radius1*6, (int)radius1*6);
            g.drawOval((int)p.data[0]*3-(int)radius2*3, (int)p.data[1]*3-(int)radius2*3, (int)radius2*6, (int)radius2*6);
            g.setColor(prev);
        }
        
        if (!displayed)
        {
            displayinwin(outp,0,400);
            displayed = true;
        }
      /*
      //If currentRect exists, paint a rectangle on top.
          if (currentRect != null) {
              Rectangle box = getDrawableRect(currentRect, d);
              //controller.rectChanged(box);

              //Draw the box outline.
              g.drawRect(box.x, box.y, box.width - 1, box.height - 1);
          }*/
    }

    Rectangle getDrawableRect(Rectangle originalRect, Dimension drawingArea) {
        
        /*
        int x = originalRect.x*3;
        int y = originalRect.y*3;
        int width = originalRect.width*3;
        int height = originalRect.height*3;*/
        
         int x = originalRect.x;
        int y = originalRect.y;
        int width = originalRect.width;
        int height = originalRect.height;

        //Make sure rectangle width and height are positive.
        if (width < 0) {
            width = 0 - width;
            x = x - width + 1;
            if (x < 0) {
                width += x;
                x = 0;
            }
        }
        if (height < 0) {
            height = 0 - height;
            y = y - height + 1;
            if (y < 0) {
                height += y;
                y = 0;
            }
        }

        //The rectangle shouldn't extend past the drawing area.
        if ((x + width) > drawingArea.width) {
            width = drawingArea.width - x;
        }
        if ((y + height) > drawingArea.height) {
            height = drawingArea.height - y;
        }

        return new Rectangle(x, y, width, height);
    }

    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(300,350);
    }

    /*
    public void drawRect(rectangle r)
    {
        currentRect = r.toRectangle();
        repaint();
    }*/

    public void displayinwin(String s,int x,int y)
    {
        //String qs="",a="";
        //int fl=0;

        Frame fq=new Frame("Rtree structure:");
        fq.resize(300,250);
        TextArea ta=new TextArea(s);
        fq.add("Center",ta);
        fq.show();
        fq.move(x,y);
    }
}