import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

//use NANO time to accurately record execution time
import java.util.concurrent.TimeUnit;

public class SQL_Connection {
	
    /* Database credentials */
    static String user = "cse4701";
    static String password = "intersect";
    static String host = "query.engr.uconn.edu";
    static String port = "1521";
    static String sid = "BIBCI";
    static String url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
	
	static Connection con = null;
	static Statement stmt;


	public static void main(String[] args) {
		// TODO Auto-generated method stub

			SQL_Connection C1 = new SQL_Connection();

		try {
				
				DriverManager.registerDriver(new oracle.jdbc.OracleDriver()); 
				
				// the connection keychain has been modified to make the file connect to desired database
				// UCONN IP required for connection
				con = DriverManager.getConnection(url, user, password);
				
	            stmt = con.createStatement();
	            
			//since the expected information is the same, it is calculated below
	            int deceased = 0;
	            int total = 0;
	            
	            //first SQL: obtain the total number of patients
	            String SQL1 = "SELECT COUNT(*) FROM IG_READY";
	            
	            ResultSet rs = stmt.executeQuery(SQL1);
	            
	            while (rs.next()) {
	            	
	                ArrayList<Object> obArray = new ArrayList<Object>();
	                
	                obArray.add(rs.getObject(1));
	                total = Integer.parseInt(obArray.toArray()[0].toString());
	                //System.out.println(total);
	                

	            }
	            
	            
	            //second SQL: obtain the number of patients DESEASED - status = 1
	            String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1";
	            
	            ResultSet rs2 = stmt.executeQuery(SQL2);
	            
	            while (rs2.next()) {
	            	
	                ArrayList<Object> obArray = new ArrayList<Object>();
	                
	                obArray.add(rs2.getObject(1));
	                deceased = Integer.parseInt(obArray.toArray()[0].toString());
	                //System.out.println(deceased);
	                

	            }
	            
	           float deceasedRatio = (float) deceased/total;
	           System.out.println(deceasedRatio);
	            
	           //using the deceased rate to calculate entropy
	           double info = - deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2));
	           System.out.println(info); 
	           
			//each of these values contains the information gain for a particular gene
			
			double InfoAPC = C1.APC_Gain(total, deceased);
			double InfoTP53 = C1.TP53_Gain(total, deceased);
			double InfoKRAS = C1.KRAS_Gain(total, deceased);
			double InfoPIK3CA = C1.PIK3CA_Gain(total, deceased);
			double InfoPTEN = C1.PTEN_Gain(total, deceased);
			double InfoATN = C1.ATN_Gain(total, deceased);
			double InfoMUC4 = C1.MUC4_Gain(total, deceased);
			double InfoSMAD4 = C1.SMAD4_Gain(total, deceased);
			double InfoSYNE1 = C1.SYNE1_Gain(total, deceased);
			double InfoFBXW7 = C1.FBXW7_Gain(total, deceased);	
			
			double IGAPC = info - InfoAPC;
			double IGTP53 = info - InfoTP53;
			double IGKRAS = info - InfoKRAS;
			double IGPIK3CA = info - InfoPIK3CA;
			double IGPTEN = info - InfoPTEN;
			double IGATN = info - InfoATN;
			double IGMUC4 = info - InfoMUC4;
			double IGSMAD4 = info - InfoSMAD4;
			double IGSYNE1 = info - InfoSYNE1;
			double IGFBXW7 = info - InfoFBXW7;	
			
			//count for each gene overlap; meaning mutation occurred and deceased
			int CountAPC = C1.count("APC");
			int CountTP53 = C1.count("TP53");
			int CountKRAS = C1.count("KRAS");
			int CountPIK3CA = C1.count("PIK3CA");
			int CountPTEN = C1.count("PTEN");
			int CountATN = C1.count("ATN");
			int CountMUC4 = C1.count("MUC4");
			int CountSMAD4 = C1.count("SMAD4");
			int CountSYNE1 = C1.count("SYNE1");
			int CountFBXW7 = C1.count("FBXW7");		
			
			//generate and print out the table for part A; sort based on information gained and print out the top five results
			GeneIGCOUNT StatAPC = new GeneIGCOUNT("APC", IGAPC, CountAPC);
			GeneIGCOUNT StatTP53 = new GeneIGCOUNT("TP53", IGTP53, CountTP53);
			GeneIGCOUNT StatKRAS = new GeneIGCOUNT("KRAS", IGKRAS, CountKRAS);
			GeneIGCOUNT StatPIK3CA = new GeneIGCOUNT("PIK3CA", IGPIK3CA, CountPIK3CA);
			GeneIGCOUNT StatPTEN = new GeneIGCOUNT("PTEN", IGPTEN, CountPTEN);
			GeneIGCOUNT StatATN = new GeneIGCOUNT("ATN", IGATN, CountATN);
			GeneIGCOUNT StatMUC4 = new GeneIGCOUNT("MUC4", IGMUC4, CountMUC4);
			GeneIGCOUNT StatSMAD4 = new GeneIGCOUNT("SMAD4", IGSMAD4, CountSMAD4);
			GeneIGCOUNT StatSYNE1 = new GeneIGCOUNT("SYNE1", IGSYNE1, CountSYNE1);
			GeneIGCOUNT StatFBXW7 = new GeneIGCOUNT("FBXW7", IGFBXW7, CountFBXW7);
			
			ArrayList<GeneIGCOUNT> resultTable = new ArrayList<GeneIGCOUNT>();
			resultTable.add(StatAPC);
			resultTable.add(StatTP53);
			resultTable.add(StatKRAS);
			resultTable.add(StatPIK3CA);
			resultTable.add(StatPTEN);
			resultTable.add(StatATN);
			resultTable.add(StatMUC4);
			resultTable.add(StatSMAD4);
			resultTable.add(StatSYNE1);
			resultTable.add(StatFBXW7);
			
			Collections.sort(resultTable, new SortByIG());
			
			System.out.println();
			System.out.format("%-15s%-15s\n", "Gene#", "IG");
			
			for(int k = 0; k < 5; k++) {
				
				GeneIGCOUNT currentTarget = (GeneIGCOUNT) resultTable.toArray()[k];
				
				System.out.format("%-15s%-15s\n", currentTarget.getGENE(), currentTarget.getIG());
			}
			
			//generate and print out the table for part B
			System.out.println();
			System.out.format("%-15s%-25s%-15s\n", "Gene#", "IG", "O COUNT");
			
			for(int k = 0; k < 5; k++) {
				
				GeneIGCOUNT currentTarget = (GeneIGCOUNT) resultTable.toArray()[k];
				
				System.out.format("%-15s%-25s%-15s\n", currentTarget.getGENE(), currentTarget.getIG(), currentTarget.getCOUNT());
			}
			
			//extra points assignment 1
			
			//first, place all the possible gene names into an array; generate GeneIGCOUNT array
			String[] geneArray = {"APC", "TP53", "KRAS", "PIK3CA", "PTEN", "ATN", "MUC4", "SMAD4", "SYNE1", "FBXW7"};
			ArrayList<GeneIGCOUNT> resultTableDoubleGene = new ArrayList<GeneIGCOUNT>();
			
			//second, nested for loop will check all the possible combinations of gene pairs
			for(int k = 0; k < 10; k++) {
				for(int m = k + 1; m < 10; m++) {
					
					System.out.println(geneArray[k] + "-" + geneArray[m]);
					
					double targetINFO = C1.doubleGene_Gain(total, deceased, geneArray[k], geneArray[m]);
					int targetCount = C1.count(geneArray[k], geneArray[m]);
					
					GeneIGCOUNT currentResult = new GeneIGCOUNT(geneArray[k] + "-" + geneArray[m], info - targetINFO, targetCount);
					resultTableDoubleGene.add(currentResult);	
					
				}
			}
			
			Collections.sort(resultTableDoubleGene, new SortByIG());
			
			System.out.println();
			System.out.format("%-15s%-25s%-15s\n", "GeneCombination#", "IG", "O COUNT");
			
			for(int k = 0; k < 5; k++) {
				
				GeneIGCOUNT currentTarget = (GeneIGCOUNT) resultTableDoubleGene.toArray()[k];
				
				System.out.format("%-15s%-25s%-15s\n", currentTarget.getGENE(), currentTarget.getIG(), currentTarget.getCOUNT());
			}
			
			
        	//close the connection when done
    		con.close();			

    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }

		
    }
	
	int count(String TARGET) {
		
		int count = 0;
		
		try {   
			
			String SQL = "SELECT COUNT(*) FROM IG_READY WHERE "+ TARGET + " = 1 AND STATUS = 1";
	        
	        ResultSet rs = stmt.executeQuery(SQL);
	        
	        while (rs.next()) {
	        	
	            ArrayList<Object> obArray = new ArrayList<Object>();
	            
	            obArray.add(rs.getObject(1));
	            count = Integer.parseInt(obArray.toArray()[0].toString());
	            //System.out.println(TARGET + "    " + count);     

	        }
			
		} catch (SQLException ex) {
	        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
	    }
		
		return count;
	}
	
	int count(String TARGET, String TARGET2) {
		
		int count = 0;
		
		try {   
			
			String SQL = "SELECT COUNT(*) FROM IG_READY WHERE "+ TARGET + " = 1 AND STATUS = 1 AND " + TARGET2 + "= 1";
	        
	        ResultSet rs = stmt.executeQuery(SQL);
	        
	        while (rs.next()) {
	        	
	            ArrayList<Object> obArray = new ArrayList<Object>();
	            
	            obArray.add(rs.getObject(1));
	            count = Integer.parseInt(obArray.toArray()[0].toString());
	            //System.out.println(TARGET + "    " + count);     

	        }
			
		} catch (SQLException ex) {
	        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
	    }
		
		return count;
	}

	double FBXW7_Gain(int TOT, int DES) {
		double info = 0;
		
        int deceased = 0;
        int total = 0;
        
	try {
		//obtain total number of FBXW7 mutation occurs in patient group
        String SQL = "SELECT COUNT(*) FROM IG_READY WHERE FBXW7 = 1";
        
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs.getObject(1));
            total = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(total);     

        }
        
		//obtain deceased with FBXW7 mutated
        String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1 AND FBXW7 = 1";
        
        ResultSet rs2 = stmt.executeQuery(SQL2);
        
        while (rs2.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs2.getObject(1));
            deceased = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(deceased);
            
        }
        
        float deceasedRatio = (float) deceased/total;
        float deceasedRatioREST = (float) (DES - deceased)/(TOT - total);
        
        info = ((double) total/TOT)*(- deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2)))
        		+ ((double) (TOT-total)/TOT)*(- deceasedRatioREST*(Math.log(deceasedRatioREST)/Math.log(2)) - (1-deceasedRatioREST)*(Math.log((1-deceasedRatioREST))/Math.log(2)));    
        
        //System.out.println(info);
		
		return info;
		
    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
	
	return 0;
	
	}

	double SYNE1_Gain(int TOT, int DES) {
		double info = 0;
		
        int deceased = 0;
        int total = 0;
        
	try {
		//obtain total number of SYNE1 mutation occurs in patient group
        String SQL = "SELECT COUNT(*) FROM IG_READY WHERE SYNE1 = 1";
        
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs.getObject(1));
            total = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(total);     

        }
        
		//obtain deceased with SYNE1 mutated
        String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1 AND SYNE1 = 1";
        
        ResultSet rs2 = stmt.executeQuery(SQL2);
        
        while (rs2.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs2.getObject(1));
            deceased = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(deceased);
            
        }
        
        float deceasedRatio = (float) deceased/total;
        float deceasedRatioREST = (float) (DES - deceased)/(TOT - total);
        
        info = ((double) total/TOT)*(- deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2)))
        		+ ((double) (TOT-total)/TOT)*(- deceasedRatioREST*(Math.log(deceasedRatioREST)/Math.log(2)) - (1-deceasedRatioREST)*(Math.log((1-deceasedRatioREST))/Math.log(2)));    
        
        //System.out.println(info);
		
		return info;
		
    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
	
	return 0;
	}

	double SMAD4_Gain(int TOT, int DES) {
		double info = 0;
		
        int deceased = 0;
        int total = 0;
        
	try {
		//obtain total number of SMAD4 mutation occurs in patient group
        String SQL = "SELECT COUNT(*) FROM IG_READY WHERE SMAD4 = 1";
        
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs.getObject(1));
            total = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(total);     

        }
        
		//obtain deceased with SMAD4 mutated
        String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1 AND SMAD4 = 1";
        
        ResultSet rs2 = stmt.executeQuery(SQL2);
        
        while (rs2.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs2.getObject(1));
            deceased = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(deceased);
            
        }
        
        float deceasedRatio = (float) deceased/total;
        float deceasedRatioREST = (float) (DES - deceased)/(TOT - total);
        
        info = ((double) total/TOT)*(- deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2)))
        		+ ((double) (TOT-total)/TOT)*(- deceasedRatioREST*(Math.log(deceasedRatioREST)/Math.log(2)) - (1-deceasedRatioREST)*(Math.log((1-deceasedRatioREST))/Math.log(2)));    
        
        //System.out.println(info);
		
		return info;
		
    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
	
	return 0;
	}

	double MUC4_Gain(int TOT, int DES) {
		double info = 0;
		
        int deceased = 0;
        int total = 0;
        
	try {
		//obtain total number of MUC4 mutation occurs in patient group
        String SQL = "SELECT COUNT(*) FROM IG_READY WHERE MUC4 = 1";
        
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs.getObject(1));
            total = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(total);     

        }
        
		//obtain deceased with MUC4 mutated
        String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1 AND MUC4 = 1";
        
        ResultSet rs2 = stmt.executeQuery(SQL2);
        
        while (rs2.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs2.getObject(1));
            deceased = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(deceased);
            
        }
        
        float deceasedRatio = (float) deceased/total;
        float deceasedRatioREST = (float) (DES - deceased)/(TOT - total);
        
        info = ((double) total/TOT)*(- deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2)))
        		+ ((double) (TOT-total)/TOT)*(- deceasedRatioREST*(Math.log(deceasedRatioREST)/Math.log(2)) - (1-deceasedRatioREST)*(Math.log((1-deceasedRatioREST))/Math.log(2)));    
        
        //System.out.println(info);
		
		return info;
		
    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
	
	return 0;
	}

	double ATN_Gain(int TOT, int DES) {
		double info = 0;
		
        int deceased = 0;
        int total = 0;
        
	try {
		//obtain total number of ATN mutation occurs in patient group
        String SQL = "SELECT COUNT(*) FROM IG_READY WHERE ATN = 1";
        
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs.getObject(1));
            total = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(total);     

        }
        
		//obtain deceased with ATN mutated
        String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1 AND ATN = 1";
        
        ResultSet rs2 = stmt.executeQuery(SQL2);
        
        while (rs2.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs2.getObject(1));
            deceased = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(deceased);
            
        }
        
        float deceasedRatio = (float) deceased/total;
        float deceasedRatioREST = (float) (DES - deceased)/(TOT - total);
        
        info = ((double) total/TOT)*(- deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2)))
        		+ ((double) (TOT-total)/TOT)*(- deceasedRatioREST*(Math.log(deceasedRatioREST)/Math.log(2)) - (1-deceasedRatioREST)*(Math.log((1-deceasedRatioREST))/Math.log(2)));    
        
        //System.out.println(info);
		
		return info;
		
    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
	
	return 0;
	}

	double PTEN_Gain(int TOT, int DES) {
		double info = 0;
		
        int deceased = 0;
        int total = 0;
        
	try {
		//obtain total number of PTEN mutation occurs in patient group
        String SQL = "SELECT COUNT(*) FROM IG_READY WHERE PTEN = 1";
        
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs.getObject(1));
            total = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(total);     

        }
        
		//obtain deceased with PTEN mutated
        String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1 AND PTEN = 1";
        
        ResultSet rs2 = stmt.executeQuery(SQL2);
        
        while (rs2.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs2.getObject(1));
            deceased = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(deceased);
            
        }
        
        float deceasedRatio = (float) deceased/total;
        float deceasedRatioREST = (float) (DES - deceased)/(TOT - total);
        
        info = ((double) total/TOT)*(- deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2)))
        		+ ((double) (TOT-total)/TOT)*(- deceasedRatioREST*(Math.log(deceasedRatioREST)/Math.log(2)) - (1-deceasedRatioREST)*(Math.log((1-deceasedRatioREST))/Math.log(2)));    
        
        //System.out.println(info);
		
		return info;
		
    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
	
	return 0;
	}

	double PIK3CA_Gain(int TOT, int DES) {
		double info = 0;
		
        int deceased = 0;
        int total = 0;
        
	try {
		//obtain total number of PIK3CA mutation occurs in patient group
        String SQL = "SELECT COUNT(*) FROM IG_READY WHERE PIK3CA = 1";
        
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs.getObject(1));
            total = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(total);     

        }
        
		//obtain deceased with PIK3CA mutated
        String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1 AND PIK3CA = 1";
        
        ResultSet rs2 = stmt.executeQuery(SQL2);
        
        while (rs2.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs2.getObject(1));
            deceased = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(deceased);
            
        }
        
        float deceasedRatio = (float) deceased/total;
        float deceasedRatioREST = (float) (DES - deceased)/(TOT - total);
        
        info = ((double) total/TOT)*(- deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2)))
        		+ ((double) (TOT-total)/TOT)*(- deceasedRatioREST*(Math.log(deceasedRatioREST)/Math.log(2)) - (1-deceasedRatioREST)*(Math.log((1-deceasedRatioREST))/Math.log(2)));    
        
        //System.out.println(info);
		
		return info;
		
    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
	
	return 0;
	}

	double TP53_Gain(int TOT, int DES) {
		double info = 0;
		
        int deceased = 0;
        int total = 0;
        
	try {
		//obtain total number of TP53 mutation occurs in patient group
        String SQL = "SELECT COUNT(*) FROM IG_READY WHERE TP53 = 1";
        
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs.getObject(1));
            total = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(total);     

        }
        
		//obtain deceased with TP53 mutated
        String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1 AND TP53 = 1";
        
        ResultSet rs2 = stmt.executeQuery(SQL2);
        
        while (rs2.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs2.getObject(1));
            deceased = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(deceased);
            
        }
        
        float deceasedRatio = (float) deceased/total;
        float deceasedRatioREST = (float) (DES - deceased)/(TOT - total);
        
        info = ((double) total/TOT)*(- deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2)))
        		+ ((double) (TOT-total)/TOT)*(- deceasedRatioREST*(Math.log(deceasedRatioREST)/Math.log(2)) - (1-deceasedRatioREST)*(Math.log((1-deceasedRatioREST))/Math.log(2)));    
        
        //System.out.println(info);
		
		return info;
		
    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
	
	return 0;
	}

	double KRAS_Gain(int TOT, int DES) {
		double info = 0;
		
        int deceased = 0;
        int total = 0;
        
	try {
		//obtain total number of KRAS mutation occurs in patient group
        String SQL = "SELECT COUNT(*) FROM IG_READY WHERE KRAS = 1";
        
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs.getObject(1));
            total = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(total);     

        }
        
		//obtain deceased with KRAS mutated
        String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1 AND KRAS = 1";
        
        ResultSet rs2 = stmt.executeQuery(SQL2);
        
        while (rs2.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs2.getObject(1));
            deceased = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(deceased);
            
        }
        
        float deceasedRatio = (float) deceased/total;
        float deceasedRatioREST = (float) (DES - deceased)/(TOT - total);
        
        info = ((double) total/TOT)*(- deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2)))
        		+ ((double) (TOT-total)/TOT)*(- deceasedRatioREST*(Math.log(deceasedRatioREST)/Math.log(2)) - (1-deceasedRatioREST)*(Math.log((1-deceasedRatioREST))/Math.log(2)));    
        
        //System.out.println(info);
		
		return info;
		
    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
	
	return 0;
	}

	double APC_Gain(int TOT, int DES) {
		double info = 0;
		
        int deceased = 0;
        int total = 0;
        
	try {
		//obtain total number of APC mutation occurs in patient group
        String SQL = "SELECT COUNT(*) FROM IG_READY WHERE APC = 1";
        
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs.getObject(1));
            total = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(total);     

        }
        
		//obtain deceased with APC mutated
        String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1 AND APC = 1";
        
        ResultSet rs2 = stmt.executeQuery(SQL2);
        
        while (rs2.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs2.getObject(1));
            deceased = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(deceased);
            
        }
        
        float deceasedRatio = (float) deceased/total;
        float deceasedRatioREST = (float) (DES - deceased)/(TOT - total);
        
        info = ((double) total/TOT)*(- deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2)))
        		+ ((double) (TOT-total)/TOT)*(- deceasedRatioREST*(Math.log(deceasedRatioREST)/Math.log(2)) - (1-deceasedRatioREST)*(Math.log((1-deceasedRatioREST))/Math.log(2)));    
        
        //System.out.println(info);
		
		return info;
		
    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
	
	return 0;
	}

	double doubleGene_Gain(int TOT, int DES, String Gene1, String Gene2) {
		double info = 0;
		
        int deceased = 0;
        int total = 0;
        
	try {
		//obtain total number of both mutation occurs in patient group
        String SQL = "SELECT COUNT(*) FROM IG_READY WHERE " + Gene1 +" = 1 AND " + Gene2 + " = 1";
        
        ResultSet rs = stmt.executeQuery(SQL);
        
        while (rs.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs.getObject(1));
            total = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(total);     

        }
        
		//obtain deceased with both genes mutated
        String SQL2 = "SELECT COUNT(*) FROM IG_READY WHERE STATUS = 1 AND " + Gene1 +" = 1 AND " + Gene2 + " = 1";
        
        ResultSet rs2 = stmt.executeQuery(SQL2);
        
        while (rs2.next()) {
        	
            ArrayList<Object> obArray = new ArrayList<Object>();
            
            obArray.add(rs2.getObject(1));
            deceased = Integer.parseInt(obArray.toArray()[0].toString());
            //System.out.println(deceased);
            
        }
        
        float deceasedRatio = (float) deceased/total;
        float deceasedRatioREST = (float) (DES - deceased)/(TOT - total);
        
        info = ((double) total/TOT)*(- deceasedRatio*(Math.log(deceasedRatio)/Math.log(2)) - (1-deceasedRatio)*(Math.log((1-deceasedRatio))/Math.log(2)))
        		+ ((double) (TOT-total)/TOT)*(- deceasedRatioREST*(Math.log(deceasedRatioREST)/Math.log(2)) - (1-deceasedRatioREST)*(Math.log((1-deceasedRatioREST))/Math.log(2)));    
        
        //System.out.println(info);
		
		return info;
		
    } catch (SQLException ex) {
        Logger.getLogger(SQL_Connection.class.getName()).log(Level.SEVERE, null, ex);
    }
	
	return 0;
	}
}