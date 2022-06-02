package com.trywithresource.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;


class MyResourceClass implements AutoCloseable{

	public void doIt(){
		System.out.println("My AutoClosing is doing");
	}

	@Override
	public void close() throws Exception {
		System.out.println("My resource");
	}
}

class User implements Serializable, Cloneable{
	private int id;
	private String name;
	private String address;

	public User(){

	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return id == user.id && Objects.equals(name, user.name) && Objects.equals(address, user.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, address);
	}

	public User(int id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				'}';
	}
}

//@SpringBootApplication
//@RestController
public class TryWithResourceApplication {

	public static void main(String[] args) {
//		SpringApplication.run(TryWithResourceApplication.class, args);

		File file = new File("C:\\Users\\Madinabonu_Alisherov\\Desktop\\test.txt");
        File file1 = new File("C:\\Users\\Madinabonu_Alisherov\\Downloads\\example\\example\\text.txt");
		//Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
        //creating black Excel sheet
		XSSFSheet sheet = workbook.createSheet("user Details");

		//Creating empty tree map
		Map<String, Object[]> userData = new TreeMap<>();
		userData.put("1", new Object[]{1, "Name", "Address"});
		userData.put("2",  new Object[]{1, "Madina", "Tashkent"});
		userData.put("3",  new Object[]{2, "Shon", "California"});
		userData.put("4", new Object[]{3, "Sam", "Russia"});

		Set<String> keySet = userData.keySet();
		int rowNum =0;

		for (String key : keySet){
			//creating row in the sheet
			Row row = sheet.createRow(rowNum++);

			Object[] objects = userData.get(key);

			int celNum=0;

			for (Object obj: objects){

				Cell cell = row.createCell(celNum++);
				//			List<User> objects = Arrays.asList(userData.get(key));
//            List<String> list = objects.stream().map(e -> e.getName()).collect(Collectors.toList());
				if (obj instanceof String){
					cell.setCellValue((String) obj);
				}else if (obj instanceof Integer){
					cell.setCellValue((Integer) obj);
				}
			}


		}

		String text = null;
		try (FileReader fileReader = new FileReader(file);
			 BufferedReader bufferedReader = new BufferedReader(fileReader);
			 MyResourceClass myResourceClass = new MyResourceClass();
			 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("test.txt"));
			 FileOutputStream out = new FileOutputStream(new File("userdata.xlsx"));
//			 FileInputStream inputFile = new FileInputStream( new File("userdata.xlsx"));
		) {
            //Creating work book instance reference of xlsx
//			XSSFWorkbook workbook1 = new XSSFWorkbook(inputFile);

			//Get the result from xlsx
//			XSSFSheet sheet1 = workbook1.getSheetAt(0);

			//Iterating over each row one by one
			Iterator<Row> rowIterator = sheet.iterator();

			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				Iterator<Cell>  cellIterator = row.cellIterator();

				while (cellIterator.hasNext()){
					Cell cell = cellIterator.next();

					switch (cell.getCellType()){

						case NUMERIC:
							System.out.println(cell.getNumericCellValue() +"t");
							break;
						case STRING:
							System.out.println(cell.getStringCellValue()+"t");
							break;
					}
				}
			}

			myResourceClass.doIt();
            bufferedWriter.write("Writing to the file!");
			workbook.write(out);
			//workbook.close no need since we used try with resource implements autoclose interface
//cloneable
			User user1 = new User(2, "SAM", "New York");
			User user2 = (User) user1.clone();

			System.out.println("Original user "+user1.getName()+" "+user1.getAddress());
			System.out.println("Cloned User  "+user2.getName()+" "+user2.getAddress());

            writeObjectToFile(user1, file1);
			writeObjectToFile(user2, file1);



			while((text = bufferedReader.readLine()) != null){
				System.out.println(text);
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found exception");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("can't read from file "+file.getName());
			e.printStackTrace();
		} catch(CloneNotSupportedException c){
			System.out.println("Clone not found exception ");

		} catch (Exception e) {
			System.out.println("My resource in catch being closed");
			e.printStackTrace();
		}

		//with try-resources, we dont need finally block
//		finally {
//			try {
//				if(bufferedReader != null){
//					bufferedReader.close();
//				}
//				if (fileReader != null){
//					fileReader.close();
//				}
//
//			} catch (IOException e) {
//				System.out.println("Problem with reading file "+file.getName()+e);
//				e.printStackTrace();
//			}
//			catch (NullPointerException ex){
//				System.out.println("File was never opened probably"+ex);
//			}

		}
		public static void writeObjectToFile(User user, File file) throws IOException{

		//serialization
			//save obj into file
		    try(FileOutputStream fileOutputStream = new FileOutputStream(file);
			  ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
			  ){
				outputStream.writeObject(user);
				outputStream.flush();
			}
		}

	}

//	@RequestMapping("/hello")
//	public static String hello(){
//		return "Hello";
//	}
//}
