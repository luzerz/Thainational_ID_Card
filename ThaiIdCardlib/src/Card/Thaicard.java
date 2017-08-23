/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Card;

import java.util.List;
import javax.smartcardio.*;
/**
 *
 * @author 00b934
 */
public class Thaicard {
    private static Card card;
    private static CardChannel channel;
    
    public static boolean ConCard(){
         try {
                 // Display the list of terminals
                 TerminalFactory factory = TerminalFactory.getDefault();
                 List<CardTerminal> terminals = factory.terminals().list();
                 CardTerminal terminal = terminals.get(0);
                 card = terminal.connect("*");
                 channel = card.getBasicChannel();
          
                byte[] select = new byte[]{(byte)0x00,(byte) 0xA4,(byte) 0x04,(byte) 0x00, (byte)0x08,(byte) 0xA0,(byte) 0x00, (byte)0x00,(byte) 0x00,(byte) 0x54, (byte)0x48, (byte)0x00,(byte) 0x01};
                byte[] Cmd2   = new byte[] {(byte)0x00,(byte) 0xc0, (byte)0x00,(byte) 0x00,(byte) 0x0d};
              
                byte[] Cmd4   = new byte[] {(byte)0x00,(byte) 0xc0, (byte)0x00,(byte) 0x00, (byte)0xd1};
                
                byte[] Cmd6   = new byte[] {(byte)0x00, (byte)0xc0,(byte) 0x00,(byte) 0x00, (byte)0x64};
                
                ResponseAPDU respApdu = channel.transmit(new CommandAPDU(select));
                return true;
                } catch(Exception e) {
                 System.out.println("Ouch: " + e.toString());
                }
         return false;
         
    }
    public static String GET_CID(){
     if(ConCard()){   
        try{
         byte[] Cmd1   = new byte[]{(byte)0x80,(byte) 0xb0, (byte)0x00,(byte) 0x04, (byte)0x02,(byte) 0x00,(byte) 0x0d};   
         ResponseAPDU respApdu2 = channel.transmit(new CommandAPDU(Cmd1));
                if (respApdu2.getSW1() == 0x90 && respApdu2.getSW2() == 0x00) {
                    byte[] data2 = respApdu2.getBytes();
                        String s = new String(data2,"TIS-620");  
                        System.out.println("CID :"+s.substring(0,13));
                        String cid = s.substring(0,13);
                        return cid;
                }
        card.disconnect(false);
        }catch(Exception e){
            System.out.println("CID :"+e);
        }
     }
        
        return null;
    }
    public static String[] GET_NAME(){
    if(ConCard()){
      try{
      byte[] Cmd3   = new byte[] {(byte)0x80, (byte)0xb0, (byte)0x00, (byte)0x11,(byte) 0x02,(byte) 0x00,(byte) 0xd1};
      ResponseAPDU respApdu3 = channel.transmit(new CommandAPDU(Cmd3));
                if (respApdu3.getSW1() == 0x90 && respApdu3.getSW2() == 0x00) {
                    byte[] data3 = respApdu3.getBytes();
                        String s = new String(data3,"TIS-620"); 
                        String[] name = s.split("#");
                        String[] lastthai = name[3].split(" ");
                        String[] lasteng  = name[6].split(" ");
                       
                        //System.out.print(bdate);
                        System.out.println("NAME THAI :"+name[0]+" "+name[1]+" "+lastthai[0]);
                        System.out.println("NAME ENG :"+name[4]+" "+lasteng[0]);
                        
                        String[] a ={name[0]+" "+name[1]+" "+lastthai[0],name[4]+" "+lasteng[0]};  
                        return a;
                } 
      card.disconnect(false);
      }catch(Exception e){
           System.out.println("NAME :"+e);
          
      }
    }
      return null;
    
   
   }
   public static String GET_BIRTH(){
    if(ConCard()){
      try{
      byte[] Cmd3   = new byte[] {(byte)0x80, (byte)0xb0, (byte)0x00, (byte)0x11,(byte) 0x02,(byte) 0x00,(byte) 0xd1};
      ResponseAPDU respApdu3 = channel.transmit(new CommandAPDU(Cmd3));
                if (respApdu3.getSW1() == 0x90 && respApdu3.getSW2() == 0x00) {
                    byte[] data3 = respApdu3.getBytes();
                        String s = new String(data3,"TIS-620"); 
                        String[] name = s.split("#");
                        String[] lasteng  = name[6].split(" ");
                        int ptr = (lasteng.length)-1;
                        System.out.println("Birth Day :"+lasteng[ptr].substring(0,8));
                        String birth = lasteng[ptr].substring(0,8);
                        return birth;
                }
                card.disconnect(false);
      }catch(Exception e){
           System.out.println("NAME :"+e);
      }
    }
      return null;
   }
   public static String GET_ADDR(){
    if(ConCard()){   
      try{
        byte[] Cmd5   = new byte[] {(byte)0x80,(byte) 0xb0,(byte) 0x15,(byte) 0x79,(byte) 0x02,(byte) 0x00, (byte)0x64};
         ResponseAPDU respApdu5 = channel.transmit(new CommandAPDU(Cmd5));
                if (respApdu5.getSW1() == 0x90 && respApdu5.getSW2() == 0x00) {
                    byte[] data5 = respApdu5.getBytes();
                        String s = new String(data5,"TIS-620"); 
                        String[] split = s.split("#");
                        System.out.println("ADDR:"+split[0]+" "+split[3]+" "+split[5]+" "+split[6]+" "+split[7]);
                        String address = split[0]+" "+split[3]+" "+split[5]+" "+split[6]+" "+split[7];
                        return   address;  
                }
      }catch(Exception e){
           System.out.println("Address Error :"+e);
      }
    }
      return null;
   }
   public static String[] GET_ISSUE_AND_EXP(){
    if(ConCard()){
      try{
        byte[] Cmd7   = new byte[] {(byte)0x80,(byte) 0xb0,(byte) 0x01, (byte)0x67, (byte)0x02,(byte) 0x00, (byte)0x12};
          ResponseAPDU respApdu6 = channel.transmit(new CommandAPDU(Cmd7));
                if (respApdu6.getSW1() == 0x90 && respApdu6.getSW2() == 0x00) {
                    byte[] data6 = respApdu6.getBytes();
                        String s = new String(data6,"TIS-620");  
                        System.out.println("Date Issue: "+s.substring(0,4)+"/"+s.substring(4,6)+"/"+s.substring(6,8));
                        System.out.println("Date Expiry:"+s.substring(8,12)+"/"+s.substring(12,14)+"/"+s.substring(14,16));
                        System.out.println();
                        String[] isexp = {s.substring(0,4)+"/"+s.substring(4,6)+"/"+s.substring(6,8),s.substring(8,12)+"/"+s.substring(12,14)+"/"+s.substring(14,16)};
                        return isexp;
                }
                card.disconnect(false);
      }catch(Exception e){
           System.out.println("Address Error :"+e);
      }
    }
      return null;
   }
    
}
