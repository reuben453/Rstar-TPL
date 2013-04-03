package userinterface;
import java.math.BigDecimal;
import java.util.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class TPL_algorithm {

	/**
	 * @param args
	 */
	
	Set<RTDataNode> cand_set = new HashSet<RTDataNode>(Constants.cap);
	Set<RTNode> ref_set = new HashSet<RTNode>(Constants.cap);
	PriorityQueue<RTNode> queue;
	Comparator<RTNode> comparator;
	
	TPL_algorithm()
	{
	}
	
	public float trim(PPoint query_point, Set<RTDataNode> pt_set, RTNode N)
	{
		
           	if(N.res_mbr == null)
		{
			N.res_mbr = new float[2*Constants.DIMENSION];
			for(int i = 0; i < 2*Constants.DIMENSION; i++)
				N.res_mbr[i] = N.get_mbr()[i];
		}
				
		/*N.res_mbr = */clipping(query_point, N);
		if(N.res_mbr != null)
		{
                if(N.res_mbr[0] == -1 && N.res_mbr[1] == -1)
                    return (float) Constants.MAXFLOAT;
                else
                	return Constants.MINDIST(query_point, N.res_mbr);
		}
        
                /*if(N.res_mbr==null)
                {    System.out.println("N.res_mbr is null");
                        return (float) Constants.MAXFLOAT;
                }
		if(N.res_mbr[0] == -1)
			return (float) Constants.MAXFLOAT;*/
		
		return Constants.MINDIST(query_point, N.get_mbr());
	}
	
	/*boolean val(float slope, float mid[], PPoint q, float p[])
	{
		//float q_val = q.data[1] - mid[1] - slope*(q.data[0] - mid[0]);
		//float p_val = p[1] - mid[1] - slope*(p[0] - mid[0]);
		
		if((q_val > 0 && p_val >= 0) || (q_val < 0 && p_val <= 0))
			return true;
		return false;
	}*/
	
	public boolean compute_res_mbr(float slope, RTNode cur_node, float mid1[], PPoint q)
	{
		float x1 = 0, y1 = 0, q_val, pt_left_bot = 0, pt_right_bot = 0, pt_left_top = 0, pt_right_top = 0;
		float mid[] = new float[Constants.DIMENSION];
		for(int i = 0; i < Constants.DIMENSION; i++)
			mid[i] = (float) mid1[i];
		//find a second point on the half plane, using x=100 or y=100
		/******************************slope might be infinite here*********************************/
		if(/*slope!=0 && */!Float.isInfinite(slope))
		{
			x1=0;						//x1=100 
			y1=(float) (mid[1]+slope*(x1-mid[0]));
			
			//mid[0] = (float) (Constants.MAXFLOAT);
			mid[0] = (float) 10000000;
			mid[1] = y1+slope*(mid[0]-x1);
		}
		else// if(!Float.isInfinite(slope))
		{
			y1=0;						//100
			x1=(float) ((y1-mid[1])/slope + mid[0]);
			
			
			//mid[1] = (float) (Constants.MAXFLOAT - 3E38);
			mid[1] = (float) 10000000;
			mid[0] = (mid[1]-y1)/slope + x1;
		}
		
		
		//if (slope != 0/* && !Float.isInfinite(slope)*/)
		{
			
				
			
			
			//check which side is the query
			//value of query point when put in eqn of half plane
			q_val =  (q.data[1] - mid[1] - slope*(q.data[0] - mid[0])); 
			pt_left_bot = (cur_node.get_mbr()[3] - mid[1] - slope*(cur_node.get_mbr()[0] - mid[0]));
			pt_right_bot = (cur_node.get_mbr()[3] - mid[1] - slope*(cur_node.get_mbr()[1] - mid[0]));
			pt_left_top = (cur_node.get_mbr()[2] - mid[1] - slope*(cur_node.get_mbr()[0] - mid[0]));
			pt_right_top = (cur_node.get_mbr()[2] - mid[1] - slope*(cur_node.get_mbr()[1] - mid[0]));
			
			if(Float.isInfinite(slope) && (cur_node.get_mbr()[0] - mid[0])==0)
			{
				pt_left_bot=0;
				pt_left_top=0;
			}
			if(Float.isInfinite(slope) && (cur_node.get_mbr()[1] - mid[0])==0)
			{
				pt_right_bot=0;
				pt_right_top=0;
			}
			
			if((pt_left_bot < 0 && pt_right_bot < 0 && pt_left_top < 0 && pt_right_top < 0 && q_val > 0) || (pt_left_bot > 0 && pt_right_bot > 0 && pt_left_top > 0 && pt_right_top > 0 && q_val < 0))
			{
				cur_node.res_mbr[0] = cur_node.res_mbr[1] = cur_node.res_mbr[2] = cur_node.res_mbr[3] = -1;
				return true;
			}
			
			
			/*		
			if((q_val < 0 && pt_left_bot <= 0) || (q_val > 0 && pt_left_bot >= 0))
				pt_left_bot = 0;             // 0 means it shd be kept
			if((q_val < 0 && pt_right_bot <= 0) || (q_val > 0 && pt_right_bot >= 0))
				pt_right_bot = 0;
			if((q_val < 0 && pt_left_top <= 0) || (q_val > 0 && pt_left_top >= 0))
				pt_left_top = 0;
			if((q_val < 0 && pt_right_top <= 0) || (q_val > 0 && pt_right_top >= 0))
				pt_right_top = 0;
			*/	
		}
				
		
		
		
		
		//find where half plane intersects the mbr
		int inter=0;
		float x_int1 = 0, y_int1 = 0, x_int2 = 0, y_int2 = 0;
		int where_hit1 = 0, where_hit2 = 0;									//1 is top, 2 is bot, 3 is left, 4 is right
		//Top of MBR with Half Plane
		//System.out.println(cur_node.get_mbr()[0]+" "+ cur_node.get_mbr()[1]+" "+ cur_node.get_mbr()[2]+" "+ cur_node.get_mbr()[3]);
		if(Constants.linesIntersect(cur_node.get_mbr()[0], cur_node.get_mbr()[2], cur_node.get_mbr()[1], cur_node.get_mbr()[2], mid[0], mid[1], x1, y1))
		{
			//if half plane intersects entire top of mbr
			if(slope == 0)
			{
				if(pt_left_bot > 0 && pt_right_bot > 0 && q_val > 0 || pt_left_bot < 0 && pt_right_bot < 0 && q_val < 0)
					return false;
				else
				{
					cur_node.res_mbr[3] = cur_node.res_mbr[2];
					return true;
				}
			}
			where_hit1 = 1;
			y_int1=(float) cur_node.get_mbr()[2];
			
			x_int1=(float) ((y_int1-mid[1])/slope + mid[0]);							//eqn of half plane
			inter++;
		}
		//Bot
		if(Constants.linesIntersect(cur_node.get_mbr()[0], cur_node.get_mbr()[3], cur_node.get_mbr()[1], cur_node.get_mbr()[3], mid[0], mid[1], x1, y1))
		{
			//if half plane intersects entire bot of mbr
			if(slope == 0)
			{
				if(pt_left_top > 0 && pt_right_top > 0 && q_val > 0 || pt_left_top < 0 && pt_right_top < 0 && q_val < 0)
					return false;
				else
				{
					cur_node.res_mbr[2] = cur_node.res_mbr[3];
					return true;
				}
			}
			float temp_x, temp_y;
			temp_y=(float) cur_node.get_mbr()[3];
			temp_x=(float) ((temp_y-mid[1])/slope + mid[0]);
			if(inter==0)
			{
				where_hit1 = 2;
				y_int1 = temp_y;
				x_int1 = temp_x;
			}
			else 
			{
				where_hit2 = 2;
				y_int2 = temp_y;
				x_int2 = temp_x;
			}
			inter++;
		}
		//Left
		if(Constants.linesIntersect(cur_node.get_mbr()[0], cur_node.get_mbr()[2], cur_node.get_mbr()[0], cur_node.get_mbr()[3], mid[0], mid[1], x1, y1) && inter != 2)
		{
			if(Float.isInfinite(slope))
			{
				if(pt_right_top > 0 && pt_right_bot > 0 && q_val > 0 || pt_right_top < 0 && pt_right_bot < 0 && q_val < 0)
					return false;
				else
				{
					cur_node.res_mbr[1] = cur_node.res_mbr[0];
					return true;
				}
			}
			float temp_x;
			float temp_y;
			temp_x=(float) cur_node.get_mbr()[0];
			temp_y=(float) (mid[1]+slope*(temp_x-mid[0]));
			if(inter==0)
			{
				where_hit1 = 3;
				y_int1 = temp_y;
				x_int1 = temp_x;
			}
			else 
			{
				where_hit2 = 3;
				y_int2 = temp_y;
				x_int2 = temp_x;
			}
			inter++;
		}
		//Right
		if(Constants.linesIntersect(cur_node.get_mbr()[1], cur_node.get_mbr()[2], cur_node.get_mbr()[1], cur_node.get_mbr()[3], mid[0], mid[1], x1, y1) && inter != 2)
		{
			if(Float.isInfinite(slope))
			{
				if(pt_left_top > 0 && pt_left_bot > 0 && q_val > 0 || pt_left_top < 0 && pt_left_bot < 0 && q_val < 0)
					return false;
				else
				{
					cur_node.res_mbr[0] = cur_node.res_mbr[1];
					return true;
				}
			}
			float temp_x, temp_y;
			temp_x=(float) cur_node.get_mbr()[1];
			temp_y=(float) (mid[1]+slope*(temp_x-mid[0]));
			if(inter==0)
			{
				where_hit1 = 4;
				y_int1 = temp_y;
				x_int1 = temp_x;
			}
			else 
			{
				where_hit2 = 4;
				y_int2 = temp_y;
				x_int2 = temp_x;
			}
			//inter++;
		}
		
		
		
		
		// case where the intersections are on adjacent edges
		//float new_x = 0, new_y = 0;
		if(((where_hit1==1 && where_hit2==3)|| (where_hit1==3 && where_hit2==1))/* && pt_left_top == 0*/)
		{
			//case when half plane goes through pt_top_left or pt_top_left is alone on one side of the half-plane
			if(pt_left_bot > 0 && pt_right_bot > 0 && pt_right_top > 0 && q_val < 0 || pt_left_bot < 0 && pt_right_bot < 0 && pt_right_top < 0 && q_val > 0)
			{
				if(x_int1 >= x_int2)
					cur_node.res_mbr[1] = x_int1;
				else
					cur_node.res_mbr[1] = x_int2;
				if(y_int1 >= y_int2)
					cur_node.res_mbr[3] = y_int1;
				else
					cur_node.res_mbr[3] = y_int2;
				//cur_node.res_mbr[1] = cur_node.res_mbr[0];
				//cur_node.res_mbr[3] = cur_node.res_mbr[2];
				return true;
			}
			else if(pt_left_bot > 0 && pt_right_bot > 0 && pt_right_top > 0 && q_val > 0 || pt_left_bot < 0 && pt_right_bot < 0 && pt_right_top < 0 && q_val < 0)   
				return false;
			//case when half-plane goes through top of  mbr
			if(pt_left_top == 0 && pt_right_top == 0)
			{
				if(q_val > 0 && pt_left_bot < 0 || q_val < 0 && pt_left_bot > 0)
				{
					cur_node.res_mbr[3] = cur_node.res_mbr[2];
					return true;
				}
				else
					return false;
			}
			
			if(x_int1 >= x_int2)
				cur_node.res_mbr[1] = x_int1;
			else
				cur_node.res_mbr[1] = x_int2;
			if(y_int1 >= y_int2)
				cur_node.res_mbr[3] = y_int1;
			else
				cur_node.res_mbr[3] = y_int2;
			return true;
		}
		if(((where_hit1==1 && where_hit2==4)|| (where_hit1==4 && where_hit2==1))/* && pt_right_top == 0*/)
		{
			//case when half-plane goes through pt_top_right
			if(pt_left_bot > 0 && pt_right_bot > 0 && pt_left_top > 0 && q_val < 0 || pt_left_bot < 0 && pt_right_bot < 0 && pt_left_top < 0 && q_val > 0 )
			{
				if(x_int1 <= x_int2)
					cur_node.res_mbr[0] = x_int1;
				else
					cur_node.res_mbr[0] = x_int2;
				if(y_int1 >= y_int2)
					cur_node.res_mbr[3] = y_int1;
				else
					cur_node.res_mbr[3] = y_int2;
				//cur_node.res_mbr[0] = cur_node.res_mbr[1];
				//cur_node.res_mbr[3] = cur_node.res_mbr[2];
				return true;
			}
			else if(pt_left_bot > 0 && pt_right_bot > 0 && pt_left_top > 0 && q_val > 0 || pt_left_bot < 0 && pt_right_bot < 0 && pt_left_top < 0 && q_val < 0)
				return false;
			if(x_int1 <= x_int2)
				cur_node.res_mbr[0] = (float) x_int1;
			else
				cur_node.res_mbr[0] = (float) x_int2;
			if(y_int1 >= y_int2)
				cur_node.res_mbr[3] = (float) y_int1;
			else
				cur_node.res_mbr[3] = (float) y_int2;
			return true;
		}
		if(((where_hit1==2 && where_hit2==3)|| (where_hit1==3 && where_hit2==2))/* && pt_left_bot == 0*/)
		{
			//case when half-plane goes through pt_left_bot
			if(pt_left_top > 0 && pt_right_bot > 0 && pt_right_top > 0 && q_val < 0 || pt_left_top < 0 && pt_right_bot < 0 && pt_right_top < 0 && q_val > 0)
			{
				if(x_int1 >= x_int2)
					cur_node.res_mbr[1] = x_int1;
				else
					cur_node.res_mbr[1] = x_int2;
				if(y_int1 <= y_int2)
					cur_node.res_mbr[2] = y_int1;
				else
					cur_node.res_mbr[2] = y_int2;
				//cur_node.res_mbr[1] = cur_node.res_mbr[0];
				//cur_node.res_mbr[2] = cur_node.res_mbr[3];
				return true;
			}
			else if(pt_left_top > 0 && pt_right_bot > 0 && pt_right_top > 0 && q_val > 0 || pt_left_top < 0 && pt_right_bot < 0 && pt_right_top < 0 && q_val < 0)
				return false;
			
			//case when half-plane goes through bot of  mbr
			if(pt_right_bot == 0 && pt_left_bot == 0)
			{
				if(q_val > 0 && pt_right_top < 0 || q_val < 0 && pt_right_top > 0)
				{
					cur_node.res_mbr[2] = cur_node.res_mbr[3];
					return true;
				}
				else
					return false;
			}
			if(pt_left_bot == 0 && pt_right_bot == 0)
			if(x_int1 >= x_int2)
				cur_node.res_mbr[1] = (float) x_int1;
			else
				cur_node.res_mbr[1] = (float) x_int2;
			if(y_int1 <= y_int2)
				cur_node.res_mbr[2] = (float) y_int1;
			else
				cur_node.res_mbr[2] = (float) y_int2;
			return true;
		}
		if(((where_hit1==4 && where_hit2==2)|| (where_hit1==2 && where_hit2==4))/* && pt_right_bot == 0*/)
		{
			//case when half-plane goes through pt_right_bot
			if(pt_left_bot > 0 && pt_left_top > 0 && pt_right_top > 0 && q_val < 0 || pt_left_bot < 0 && pt_left_top < 0 && pt_right_top < 0 && q_val > 0)
			{
				if(x_int1 <= x_int2)
					cur_node.res_mbr[0] = x_int1;
				else
					cur_node.res_mbr[0] = x_int2;
				if(y_int1 <= y_int2)
					cur_node.res_mbr[2] = y_int1;
				else
					cur_node.res_mbr[2] = y_int2;
				//cur_node.res_mbr[0] = cur_node.res_mbr[1];
				//cur_node.res_mbr[2] = cur_node.res_mbr[3];
				return true;
			}
			else if(pt_left_bot > 0 && pt_left_top > 0 && pt_right_top > 0 && q_val > 0 || pt_left_bot < 0 && pt_left_top < 0 && pt_right_top < 0 && q_val < 0)
				return false;
			if(x_int1 <= x_int2)
				cur_node.res_mbr[0] = x_int1;
			else
				cur_node.res_mbr[0] = x_int2;
			if(y_int1 <= y_int2)
				cur_node.res_mbr[2] = y_int1;
			else
				cur_node.res_mbr[2] = y_int2;
			return true;
		}
		
		//case if half plane goes exactly thro diagonal
		if((pt_left_top == 0 && pt_right_bot == 0) || (pt_left_bot == 0 && pt_right_top == 0))
			return false;
		//else if((pt_left_top == 0 && pt_left_bot == 0 && pt_right_top == 0) || (pt_left_bot == 0 && pt_right_bot == 0 && pt_right_top == 0))
		//	return false;
		
		//cases where intersections are on opposite edges
		if((where_hit1==1 && where_hit2==2)|| (where_hit1==2 && where_hit2==1))
		{
			if(pt_left_top > 0 && pt_left_bot > 0 && q_val < 0 || pt_left_top < 0 && pt_left_bot < 0 && q_val > 0)
			{
				if(x_int1/* - cur_node.res_mbr[1]*/ <= x_int2/* - cur_node.res_mbr[1]*/)
					cur_node.res_mbr[0] = (float) x_int1;
				else
					cur_node.res_mbr[0] = (float) x_int2;
				
			}
			else if(pt_right_top > 0 && pt_right_bot > 0 && q_val < 0 || pt_right_top < 0 && pt_right_bot < 0 && q_val > 0)
			{
				if(x_int1 >= x_int2)
					cur_node.res_mbr[1] = (float) x_int1;
				else
					cur_node.res_mbr[1] = (float) x_int2;
				
			}
			
			//case when half-plane goes through left of  mbr
			if(pt_left_top == 0 && pt_left_bot == 0)
			{
				if(q_val > 0 && pt_right_top < 0 || q_val < 0 && pt_right_top > 0)
				{
					cur_node.res_mbr[1] = cur_node.res_mbr[0];
					return true;
				}
				else
					return false;
			}
			
			//case when half-plane goes through right of  mbr
			if(pt_right_top == 0 && pt_right_bot == 0)
			{
				if(q_val > 0 && pt_left_top < 0 || q_val < 0 && pt_left_top > 0)
				{
					cur_node.res_mbr[0] = cur_node.res_mbr[1];
					return true;
				}
				else
					return false;
			}
			return true;
		}
		
		if((where_hit1==4 && where_hit2==3)|| (where_hit1==3 && where_hit2==4))
		{
			if(pt_right_bot > 0 && pt_left_bot > 0 && q_val < 0 || pt_right_bot < 0 && pt_left_bot < 0 && q_val > 0)
			{
				if(y_int1 >= y_int2)
					cur_node.res_mbr[3] = (float) y_int1;
				else
					cur_node.res_mbr[3] = (float) y_int2;
				
			}
			else if(pt_right_top > 0 && pt_left_top > 0 && q_val < 0 || pt_right_top < 0 && pt_left_top < 0 && q_val > 0)
			{
				if(y_int1 <= y_int2)
					cur_node.res_mbr[2] = (float) y_int1;
				else
					cur_node.res_mbr[2] = (float) y_int2;
				
			}
			return true;
		}
		return false;
		
	}
	
	/**
	 * clipping algo according to paper "Processing Queries by Linear Constraints" by Goldstein et al
	 */
	public float[] clipping(PPoint q, RTNode cur_node)
	{
		float Bp[] = new float[2*Constants.DIMENSION];
		float mbr[] = cur_node.get_mbr();
		for(int i = 0; i < 2*Constants.DIMENSION; i++)
			Bp[i] = mbr[i];
		
		Iterator<RTDataNode> it = cand_set.iterator();
		//for(int i = 0; i < cand_set.size(); i++)
		while(it.hasNext())
		{
			float p[] = it.next().data[0].data;
			float mid[] = new float[Constants.DIMENSION];
			mid[0] = (p[0] + q.data[0])/2;
			mid[1] = (p[2] + q.data[1])/2;
			//float slope ;
			//if((p[2] - q.data[1])==0)
					
			float slope = -1*((p[0] - q.data[0])/(p[2] - q.data[1]));
			boolean check=compute_res_mbr(slope,cur_node, mid, q);
            if(!check)
            {
            	cur_node.res_mbr = null;
            	return Bp;
            }
            else
            	System.out.println("TRIMMED\n"+cur_node.get_mbr()[0]+" "+cur_node.get_mbr()[1]+" "+cur_node.get_mbr()[2]+" "+cur_node.get_mbr()[3]+"\n"+cur_node.get_res_mbr()[0]+" "+cur_node.get_res_mbr()[1]+" "+cur_node.get_res_mbr()[2]+" "+cur_node.get_res_mbr()[3]);
            	
            for(int i = 0; i < 2*Constants.DIMENSION; i++)
    			Bp[i] = cur_node.get_res_mbr()[i];
			
			
			//if mbr is totally clipped
			if(Bp[0]==Bp[1] && Bp[2] == Bp[3] && Bp[0] == -1 && Bp[2] == -1)
				return Bp;
			
			
		}
		return Bp;
	}
	
	/**
	 * Implements the TPL RNN algorithm
	 */
	public void Reverse_nearest_neighbour(RTree rt ,PPoint query_point)
	{
		comparator = new Comparator_RTNode(query_point);
		queue = new PriorityQueue<RTNode>(Constants.cap, comparator);
		
		queue.add(rt.root_ptr);
                
        //TPL_algorithm a= new TPL_algorithm();
		TPL_filter(query_point);
		Set<RTNode> N_ref = new HashSet<RTNode>(Constants.cap);
		Set<RTDataNode> P_ref = new HashSet<RTDataNode>(Constants.cap);
		Iterator<RTNode> it = ref_set.iterator();
		RTNode temp = null;
		while(it.hasNext())
		{
			temp = it.next();
			if(temp.num_entries == 1 && temp.level == 0)
			{
				RTDataNode temp1 = (RTDataNode) temp;
				P_ref.add(temp1);
			}
			else
				N_ref.add(temp);
		}
		TPL_refinement(query_point, P_ref, N_ref);
		return;
		//RTDirNode temp1 = (RTDirNode) rt.root_ptr;
		//queue.add(temp1.entries[0].get_son());
		//queue.add(temp1.entries[1].get_son());
		
		//System.out.println(Constants.MINDIST(query_point, queue.remove().get_mbr()));
		//System.out.println(Constants.MINDIST(query_point, queue.remove().get_mbr()));
		
		
		
		
		
		//return result;
		
	}
	
	private void Refinement_Round(PPoint query_point, Set<RTDataNode> P_ref, Set<RTNode> N_ref)
	{
		Iterator<RTDataNode> cand_it = cand_set.iterator();
		loop1: while(cand_it.hasNext())
		{
			PPoint temp1 = new PPoint(Constants.DIMENSION);
			PPoint temp2 = new PPoint(Constants.DIMENSION);
			
			RTDataNode p = cand_it.next();
			Iterator<RTDataNode> it1 = P_ref.iterator();
			temp1.data = new float[Constants.DIMENSION];
			temp1.data[0] = p.data[0].data[0];
			temp1.data[1] = p.data[0].data[2];
			while(it1.hasNext())
			{
				RTDataNode p_ = it1.next();
				//if(p_ == p)
				//	continue;
								
				temp2.data = new float[Constants.DIMENSION];
				temp2.data[0] = p_.data[0].data[0];
				temp2.data[1] = p_.data[0].data[2];
				if(Constants.objectDIST(temp1, temp2) < Constants.objectDIST(query_point, temp1))
				{
					cand_set.remove(temp1);
					continue loop1;
				}
			}
			Iterator<RTNode> it = N_ref.iterator();
			while(it.hasNext())
			{
				RTNode temp = it.next();
				if(Constants.MINMAXDIST(temp1, temp.get_mbr()) < Constants.objectDIST(temp1, query_point))
				{
					cand_set.remove(temp1);
					continue loop1;
				}
			}
			Iterator<RTNode> it11 = N_ref.iterator();
			while(it11.hasNext())
			{
				RTNode N_temp = it11.next();
				if(Constants.MINDIST(temp1, N_temp.get_mbr()) < Constants.objectDIST(temp1, query_point))
					p.toVisit.add(N_temp);
			}
			if(p.toVisit != null && !p.toVisit.isEmpty())
			{
				cand_set.remove(p);
				System.out.println("************RESULT FOUND***********"+p.data[0].data[0]+" "+p.data[0].data[2]);
			}
		}
		
		
	}
	
	private void TPL_refinement(PPoint query_point, Set<RTDataNode> P_ref, Set<RTNode> N_ref)
	{
		Iterator<RTDataNode> cand_it = cand_set.iterator();
		loop1: while(cand_it.hasNext())
		{
			RTDataNode p = cand_it.next();
			Iterator<RTDataNode> cand_it1 = cand_set.iterator();
			
			PPoint temp1 = new PPoint(Constants.DIMENSION);
			PPoint temp2 = new PPoint(Constants.DIMENSION);
			temp1.data = new float[Constants.DIMENSION];
			temp1.data[0] = p.data[0].data[0];
			temp1.data[1] = p.data[0].data[2];
			while(cand_it1.hasNext())
			{
				RTDataNode p_ = cand_it1.next();
				if(p_ == p)
					continue;
				
				temp2.data = new float[Constants.DIMENSION];
				temp2.data[0] = p_.data[0].data[0];
				temp2.data[1] = p_.data[0].data[2];
				if(Constants.objectDIST(temp1, temp2) < Constants.objectDIST(query_point, temp1))
				{
					cand_set.remove(temp1);
					continue loop1;
				}
			}
			if(cand_set.contains(p))
				p.toVisit = new HashSet<RTNode>();
				
		}
		Refinement_Round(query_point, P_ref, N_ref);
		if(cand_set.isEmpty())
			return;
		N_ref = null;
		P_ref = null;
		RTNode N = findMin();
		if(N == null)
			return;
		Iterator<RTDataNode> cand_it1 = cand_set.iterator();
		while(cand_it1.hasNext())
			cand_it1.next().toVisit.remove(N);
		if(N.level == 0)
		{
			for(int i = 0; i < N.num_entries; i++)
			{
				RTDataNode temp = new RTDataNode(N.my_tree, (RTDataNode)N, i);
				P_ref.add(temp);
			}
		}
		else
		{
			for(int i = 0; i < N.num_entries; i++)
				N_ref.add(((RTDirNode)N).entries[i].get_son());
			
		}
	}
	
	RTNode findMin()
	{
		class Node_temp
		{
			RTNode node;
			int count;
		}
		LinkedList<Node_temp> list = new LinkedList();
		Node_temp temp = new Node_temp();
		Iterator<RTDataNode> it = cand_set.iterator();
		
		while(it.hasNext())
		{
			RTDataNode t = it.next();
			if(t.toVisit != null && !t.toVisit.isEmpty())
			{
				Iterator<RTNode> it1 = t.toVisit.iterator();
				while(it1.hasNext())
				{
					RTNode temp1 = it1.next();
					temp.node = temp1;
					temp.count = 1;
					if(list.contains(temp1))
						list.get(list.indexOf(temp1)).count++;
					else
						list.add(temp);
				}
				//temp.addAll(t.toVisit);
			}
		}
		Node_temp least = null;
		Node_temp t;
		for(Iterator it1 = list.iterator(); it1.hasNext(); )
		{
			t = (Node_temp) it1.next();
			if(least == null)
				least = t;
			else
			{
				if(t.node.level < least.node.level)
					least = t;
				else
					if(t.count > least.count)
						least = t;
			}
		}
		if(least == null)
			return null;
		return least.node;
	}
	
	private void TPL_filter(PPoint query_point)
	{
		RTNode temp;
		while(queue.isEmpty() == false)
		{
                    
			temp = queue.remove();
            if(temp==null)
            	System.out.println("Null Temp");
            System.out.println("Removed From Queue" + temp.get_mbr()[0] +" " + temp.get_mbr()[1] + " " +temp.get_mbr()[2] +" " + temp.get_mbr()[3]);
			if (trim(query_point, cand_set, temp) == Constants.MAXFLOAT)
				ref_set.add(temp);
			else
			{
				if(temp.level == 0 && temp.num_entries == 1)
				{
					RTDataNode temp1 = (RTDataNode) temp;
					cand_set.add(temp1);
				}
				else if(temp.level == 0 && temp.num_entries > 1)
				{
					RTDataNode temp1 = (RTDataNode) temp;
					
					//RTDataNode temp2;
					for(int i = 0; i < temp1.get_num(); i++)
					{
						RTDataNode temp2 = new RTDataNode(temp.my_tree, temp1, i);
						//temp2 = (RTDataNode) temp1;
						if(trim(query_point,cand_set,temp2) != Constants.MAXFLOAT)
							queue.add(temp2);
						else
							ref_set.add(temp2);						
					}
				}
				else
				{
					RTDirNode temp1 = (RTDirNode) temp;
					for(int i = 0; i < temp1.num_entries; i++)
					{
						float temp_dist = trim(query_point, cand_set, temp1.entries[i].get_son());
						if(temp_dist == Constants.MAXFLOAT)
							ref_set.add(temp1.entries[i].get_son());
						else
							queue.add(temp1.entries[i].get_son());
					}
				}
			}
		}
	}
	public void call(RTree rt ,PPoint query_point)
        {
            Reverse_nearest_neighbour(rt, query_point);
        }
	//public static void main(String[] args) {
	//}

}

