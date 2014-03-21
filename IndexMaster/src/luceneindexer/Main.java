/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luceneindexer;

import java.io.BufferedReader;
import luceneindexer.data.DBpedia;
import luceneindexer.data.LongAbstract;
import luceneindexer.data.ShortAbstract;
import luceneindexer.files.FileOpener;
import luceneindexer.files.LuceneWriter;

public class Main {

	private static LuceneWriter longWriter = new LuceneWriter("LongIndex");
	
	public static void main(String[] args) {
		System.out.println("Hello world");
		//Open the file of OWL for reading
//		FileOpener fOpener = new FileOpener("labels_en.ttl");
//		FileOpener shortOpener = new FileOpener("short_abstract.ttl");
		FileOpener longOpener = new FileOpener("long_abstracts_en.ttl");

		//Create the object for writing
//		LuceneWriter luceneWriter = new LuceneWriter("indexDir");
//		LuceneWriter shortWriter = new LuceneWriter("ShortIndex");

//		try {
//
//			//first see if we can open a directory for writing
//			if (luceneWriter.openIndex()){
//				//get a buffered reader handle to the file
//				BufferedReader breader = new BufferedReader(fOpener.getFileForReading());
//				//loop through the file line by line and parse
//				String line;
//
//				while((line = breader.readLine()) != null){
//
//					DBpedia dBpedia = new DBpedia();
//						if(line.contains("label")){
//							dBpedia.setResource(line.substring(1, (line.indexOf(">"))));
//							dBpedia.setLabel(line.substring(line.indexOf("label")+ 8));
//							luceneWriter.addDBpedia(dBpedia);
//						}
//				}
//
//			} else {
//				System.out.println("We had a problem opening the directory for writing");
//			}
//
//
//		} 
//		catch (Exception e) {
//			System.out.println("Threw exception " + e.getClass() + " :: " + e.getMessage());
//
//		} 
//		finally {
//			//close out the index and release the lock on the file
//			luceneWriter.finish();
//		}
//		
//
//		try {
//
//			//first see if we can open a directory for writing
//			if (shortWriter.openIndex()){
//				//get a buffered reader handle to the file
//				BufferedReader breader = new BufferedReader(shortOpener.getFileForReading());
//				//loop through the file line by line and parse
//				String line;
//
//				while((line = breader.readLine()) != null){
//					ShortAbstract shortAbstract = new ShortAbstract();
//						if(line.contains("comment")){
//							shortAbstract.setResource(line.substring(1, (line.indexOf(">"))));
//							shortAbstract.setShort(line.substring(line.indexOf("comment")+ 10));
//							shortWriter.addShortAbstract(shortAbstract);
//						}
//				}
//
//			} else {
//				System.out.println("We had a problem opening the directory for writing");
//			}
//
//
//		} 
//		catch (Exception e) {
//			System.out.println("Threw exception 2" + e.getClass() + " :: " + e.getMessage());
//
//		} 
//		finally {
//			//close out the index and release the lock on the file
//			shortWriter.finish();
//		}
		
		
		try {

			//first see if we can open a directory for writing
			if (getLongWriter().openIndex()){
				//get a buffered reader handle to the file
				BufferedReader breader = new BufferedReader(longOpener.getFileForReading());
				//loop through the file line by line and parse
				String line;

				while((line = breader.readLine()) != null){

					LongAbstract longAbstract = new LongAbstract();
						if(line.contains("abstract")){
							longAbstract.setResource(line.substring(1, (line.indexOf(">"))));
							longAbstract.setLongAbstract(line.substring(line.indexOf("abstract")+ 11));
							getLongWriter().addLongAbstract(longAbstract);
						}
						
				}
				
			
			} else {
				System.out.println("We had a problem opening the directory for writing");
			}


		} 
		catch (Exception e) {
			System.out.println("Threw exception " + e.getClass() + " :: " + e.getMessage());

		}
		finally {
			//close out the index and release the lock on the file
			getLongWriter().finish();
		}
		 
		
	
	}

	public static LuceneWriter getLongWriter() {
		return longWriter;
	}


}
