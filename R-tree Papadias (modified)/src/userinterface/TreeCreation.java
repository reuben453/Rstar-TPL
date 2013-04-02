/* Modified from Test.java created by Nikos
*/
package userinterface;


public class TreeCreation {
    TreeCreation (String filename, int numRects, int dimension, int blockLength, int cacheSize) {
        this.numRects = numRects;
        this.dimension = dimension;
        this.blockLength = blockLength;
        this.cacheSize = cacheSize;
        
        // initialize tree
        rt = new RTree(filename, blockLength, cacheSize, dimension);

        // insert random data into the tree
        Data d;
        
            // create a new Data with dim=dimension
            d = new Data(dimension, 0);
            // create a new rectangle
            //rectangle r = new rectangle(i);
            // copy the rectangle's coords into d's data
            d.data = new float[dimension*2];
          /*  d.data[0] = 50;
            d.data[1] = 50;
            d.data[2] = 100;
            d.data[3] = 100;
            //d.print();
            rt.insert(d);
        
           
            d.data[0] = 75;
            d.data[1] = 75;
            d.data[2] = 125;
            d.data[3] = 125;
            //d.print();
            rt.insert(d);
            
            d.data[0] = 275;
            d.data[1] = 275;
            d.data[2] = 25;
            d.data[3] = 25;
            
            
            rt.insert(d);
            
            d.data[0] = 175;
            d.data[1] = 175;
            d.data[2] = 125;
            d.data[3] = 125;
            
            
            rt.insert(d);
          */  
             for (int i=0; i<numRects; i++)
        {
            // create a new Data with dim=dimension
            d = new Data(dimension, i);
            // create a new rectangle
            rectangle r = new rectangle(i);
            // copy the rectangle's coords into d's data
            d.data = new float[dimension*2];
            d.data[0] = (float)r.LX;
            d.data[1] = (float)r.LX;
            d.data[2] = (float)r.LY;
            d.data[3] = (float)r.LY;
            //d.print();
            rt.insert(d);
        }
          
        
        // Create the Query Result Window
        //qf = new QueryFrame(rt);
        //qf.show();
        //qf.move(400, 0);

        // Create the Rectangle Display Window
        f = new RectFrame(this);
        f.pack();
        f.show();

    }
    
    TreeCreation (String filename, int cacheSize) {
        //this.numRects = numRects;
        //this.dimension = dimension;
        //this.blockLength = blockLength;
        this.cacheSize = cacheSize;
        
        // initialize tree
        rt = new RTree(filename, cacheSize);

        // Create the Rectangle Display Window
        f = new RectFrame(this);
        f.pack();
        f.show();

    }
    public void display()
    {
         f = new RectFrame(this);
        f.pack();
        f.show();
    }
    public void exit(int exitcode)
    {
        if ((rt != null) && (exitcode == 0))
            rt.delete();
        System.exit(0);
    }

    public RTree rt;
    public RectFrame f;
    //public QueryFrame qf;
    public int displaylevel = 199;
    private int numRects, dimension, blockLength, cacheSize;
}