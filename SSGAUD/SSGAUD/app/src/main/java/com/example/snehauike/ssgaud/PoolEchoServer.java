package com.example.snehauike.ssgaud;

/**
 * Created by snehauike on 11/10/16.
 */

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PoolEchoServer extends Thread {

    public final static int defaultPort = 5554;//port no of the device

    ServerSocket theServer; //socket

    static int num_threads = 10;

    public static void main(String[] args) {

        int port = defaultPort;
        try {
            port = Integer.parseInt(args[0]);
        }
        catch (Exception e) { }

        if (port <= 0 || port >= 65536) port = defaultPort;//check port no range

        try {
            ServerSocket ss = new ServerSocket(port);

            System.out.println("Server Socket Start!!");

            for (int i = 0; i < num_threads; i++) {
                System.out.println("Create num_threads " + i + " Port:" + port);
                PoolEchoServer pes = new PoolEchoServer(ss);
                pes.start();
            }
        }
        catch (IOException e) { System.err.println(e);
        }
    }//end of main

    public PoolEchoServer(ServerSocket ss) { //constructor
        theServer = ss;
    }

    public void run() { while (true) try {
        DataOutputStream output;
        DataInputStream input;
        Socket connection = theServer.accept();
        System.out.println("Accept Client!");
        input = new DataInputStream(
                connection.getInputStream());
        output = new DataOutputStream(
                connection.getOutputStream());

        System.out.println("Client Connected and Start get I/O!!");

        while (true) {
            String longitude = input.readUTF();
            String latitude = input.readUTF();

            String excelFilePath = "/Users/snehauike/AndroidStudioProjects/SSGAUD/app/src/main/Assets/excel.xls";

            try{
                FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
                HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                HSSFSheet sheet = workbook.getSheetAt(0);
                Cell cell = null;

                //update the value of cell
                cell = sheet.getRow(1).getCell(0);
                cell.setCellValue(latitude);

                cell = sheet.getRow(1).getCell(1);
                cell.setCellValue(longitude);
                //Row row = sheet.getRow(0);
                inputStream.close();



                FileOutputStream outFile = new FileOutputStream(new File(excelFilePath));
                workbook.write(outFile);
                outFile.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            System.out.println("==> Input from Client: longitude " + longitude);

            System.out.println("==> Input from Client: latitude " + latitude);

            System.out.println("Output to Client ==> \"Connection successful\"");

            //read from excel file
            try{
                FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
                HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                HSSFSheet sheet = workbook.getSheetAt(0);

                Cell cell = null;

                //update the value of cell
                cell = sheet.getRow(18).getCell(1);
                output.writeUTF(String.valueOf(cell));


            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //output.writeUTF("Connection successful");

            output.flush();
        } // end while
    } // end try
    catch (IOException e) {
    }
        // end while
    } // end run
 }