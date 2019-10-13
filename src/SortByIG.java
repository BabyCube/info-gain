import java.util.Comparator;

public class SortByIG implements Comparator<GeneIGCOUNT> {

	@Override
	public int compare(GeneIGCOUNT o1, GeneIGCOUNT o2) {
		
		//if info is bigger then return a positive value
		if(o1.getIG() - o2.getIG() > 0) {
			return -1;
		}else if (o1.getIG() - o2.getIG() < 0) {
			return 1;
		}
		
		return 0;
		
	}

}
