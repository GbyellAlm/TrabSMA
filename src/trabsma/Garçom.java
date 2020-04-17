/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabsma;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Almeida Gabriel
 */
public class Garçom extends Agent{
    
    private String pedido;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // TODO code application logic here
    }
    
    @Override
    protected void setup(){
        System.out.println("Olá,");
        System.out.println("Sou o garçom " + getLocalName() + ". Qual o seu pedido?");
        
        Object [] arg = getArguments();
        
        if(arg != null && arg.length > 0){
            System.out.println("Beleza! Só aguardar um pouquinho.");
            
            pedido = (String) arg[0];
            addBehaviour(new OneShotBehaviour(this){
                @Override
                public void action(){
                    ACLMessage msgPedido = new ACLMessage (ACLMessage.INFORM);
                        msgPedido.addReceiver(new AID ("Caue", AID.ISLOCALNAME));
                        msgPedido.setLanguage("Português");
                        msgPedido.setOntology("Obrigação");
                        msgPedido.setContent(pedido);
                        myAgent.send(msgPedido);
                }
                
            });
        }
        
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage respImediataRefAoPedido = myAgent.receive();
                if (respImediataRefAoPedido != null){
                    String content = respImediataRefAoPedido.getContent();
                    System.out.println("--> " + respImediataRefAoPedido.getSender().getName() + ": " + content);
                }else{
                    block();
                }
            }
        });
        
        addBehaviour(new OneShotBehaviour(this){
            @Override
            public void action() {
                ACLMessage pedido = myAgent.receive();
                String pedidoo = pedido.getContent();
                
                if(pedido != null){
                    if("Pizza de calabresa prontinha!".equals(pedidoo)){
                        System.out.println("Está aqui sua pizza de calabresa! c:");
                    }else{
                        System.out.println("Sentimos muito, estamos sem ingredientes suficiente para fazer sua pizza de calabresa. :c");
                    }
                }
            }
        });
    }
}
