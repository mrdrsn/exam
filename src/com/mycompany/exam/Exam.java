package com.mycompany.exam;

import controller.Controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Measurement;
import view.GUI;
import view.GUIMeasurement;

/**
 *
 * @author Nastya
 */
public class Exam {

    public static void main(String[] args) throws IOException {
        GUIMeasurement g = new GUIMeasurement();
//        GUI g = new GUI();
//        Controller controller = new Controller();
//        Map<String, String> patientInfo = controller.getAllPatientFullNames();
//        for (Map.Entry<String, String> entry : patientInfo.entrySet()) {
//            System.out.println(entry);
//        }
//        controller.addPatient("а а а", "P016");
////        controller.setPatient("P007")
//        List<String> recentTemps = controller.getTemperaturesFromMeasurements("P007");
//        try{
//            for(String t: recentTemps){
//                System.out.println(t);
//            }
//        } catch(NullPointerException ex){
//            System.out.println("-");
//        }
//        
//        List<Measurement> measurements = controller.getLastMeasurements("P007", 10);
//        try{
//            for(Measurement m: measurements){
//                System.out.println(m);
//            }
//        } catch(NullPointerException ex){
//            System.out.println("-");
//        }
//        System.out.println(controller.getAllPatientFullNames());
//        System.out.println(controller.getAllPatientIds());
//        List<String> patientFullNames = new ArrayList<>();
//        for(String id: controller.getAllPatientIds()){
//            patientFullNames.add(controller.parseFullNameFromLog(id));
////            System.out.println(controller.parseFullNameFromLog(id));
//        }
//        System.out.println(patientFullNames);
    }
}
