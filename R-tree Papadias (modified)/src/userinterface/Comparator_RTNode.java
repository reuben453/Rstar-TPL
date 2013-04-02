package userinterface;

import java.util.Comparator;

public class Comparator_RTNode implements Comparator<RTNode>
{
	PPoint query_p;
	Comparator_RTNode(PPoint p)
	{
		query_p = p;
	}
    @Override
    public int compare(RTNode node1, RTNode node2) 
    {
    	if(node1.get_mbr()[0] == -1 || node2.get_mbr()[0] == -1)
    		System.out.println("Error");
    	if(Constants.MINDIST(query_p, node1.get_mbr()) < Constants.MINDIST(query_p, node2.get_mbr()))
    		return -1;
    	else if(Constants.MINDIST(query_p, node1.get_mbr()) > Constants.MINDIST(query_p, node2.get_mbr()))
    		return 1;
		return 0;
	}
}
