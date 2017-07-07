package com.android.susmita.ssgaud;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {
    public static void main(String[] args){
        Socket clientSocket = null;
        ServerSocket server = null;



        try{
            //start the server at port number 5554
            server = new ServerSocket(5554);
            System.out.println("Server Started.Waiting for client request");
            while(true){
                System.out.println("Waiting for new requests");
                clientSocket = server.accept();

                //create a thread to handle multiple clients together and multiple requets from same cline
                Thread stuffer = new Server().new StuffThread(clientSocket);
                //start the thread
                stuffer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }//end of main

    class StuffThread extends Thread{
        private byte[] data = new byte[255];
        private Socket socket;



        public StuffThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String excelFilePath = "/Users/snehauike/AndroidStudioProjects/SSGAUD/app/src/main/Assets/excel.xls";

            try{
                FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
                HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                HSSFSheet sheet = workbook.getSheetAt(0);
                Cell cell = null;

                //update the value of cell
                cell = sheet.getRow(1).getCell(0);
                cell.setCellValue("47");
                //Row row = sheet.getRow(0);
                inputStream.close();

                FileOutputStream outFile = new FileOutputStream(new File("/Users/snehauike/AndroidStudioProjects/SSGAUD/app/src/main/Assets/excel.xls"));
                workbook.write(outFile);
                outFile.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }//end of stuffthread


}//edn of class
