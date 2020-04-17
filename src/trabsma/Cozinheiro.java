/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabsma;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Almeida Gabriel
 */
public class Cozinheiro extends Agent{
    
    String ingredientes;
  
    @Override
    protected void setup(){
        addBehaviour(new CyclicBehaviour(this){
           @Override
           public void action(){
               ACLMessage msgPedido = myAgent.receive();
               if(msgPedido != null){
                    String pedido = msgPedido.getContent();
                    
                    ACLMessage respImediataRefAoPedidoProGarçom = msgPedido.createReply();
                    respImediataRefAoPedidoProGarçom.setPerformative(ACLMessage.INFORM);
                    respImediataRefAoPedidoProGarçom.setContent("Beleza "+msgPedido.getSender().getName()+"! Pedido recebido. Vou só verificar se há ingredientes suficientes em estoque para prepara-lo, e prepara-lo - se tiver.");
                    myAgent.send(respImediataRefAoPedidoProGarçom);
                    
                    if("Pizza de Calabresa".equalsIgnoreCase(pedido)){
                        ingredientes = "1 Massa de pizza, 1 Molho de tomate, 2 Calabresas, 8 Azeitonas pretas e 1 Cebola.";
                    
                    ACLMessage msgProEstoque = new ACLMessage(ACLMessage.INFORM);
                        msgProEstoque.addReceiver(new AID ("EstoqueComputadorizado", AID.ISLOCALNAME));
                        msgProEstoque.setLanguage("Português");
                        msgProEstoque.setOntology("Obrigação");
                        msgProEstoque.setContent(ingredientes);
                        myAgent.send(msgProEstoque);
                    
                    }else{
                        System.out.println("O pedido não é Pizza de Calabresa.");
                        block();
                    }
               }else{
                   System.out.println("Eu "+getLocalName()+" não recebi nenhum pedido!");
                   block();
               }
           } 
        });
        
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage respImediataRefAosIngredientes = myAgent.receive();
                if (respImediataRefAosIngredientes != null){
                    String content = respImediataRefAosIngredientes.getContent();
                    System.out.println("--> " + respImediataRefAosIngredientes.getSender().getName() + ": " + content);
                }else{
                    block();
                }
            }
        });
        
        addBehaviour(new CyclicBehaviour(this){
            @Override
            public void action(){
                ACLMessage respDefRefAosIngredientes = myAgent.receive();
                String ingredientes = respDefRefAosIngredientes.getContent();
                
                if(respDefRefAosIngredientes != null){
                    if("Há ingredientes suficientes!".equals(ingredientes)){
                        System.out.println("Ok! ***Fazendo Pizza de Calabresa***.");
                        
                        ACLMessage pedidoPronto = new ACLMessage(ACLMessage.INFORM);
                            pedidoPronto.addReceiver(new AID("Gabriel", AID.ISLOCALNAME));
                            pedidoPronto.setLanguage("Português");
                            pedidoPronto.setOntology("Obrigação");
                            pedidoPronto.setContent("Pizza de calabresa prontinha!");
                            myAgent.send(pedidoPronto);
                    }else{
                        System.out.println("Aff.");
                        
                        ACLMessage pedidoCancelado = new ACLMessage(ACLMessage.INFORM);
                            pedidoCancelado.addReceiver(new AID("Gabriel", AID.ISLOCALNAME));
                            pedidoCancelado.setLanguage("Português");
                            pedidoCancelado.setOntology("Obrigação");
                            pedidoCancelado.setContent("Gabriel, infelizmente estamos sem ingredientes suficientes. :c");
                            myAgent.send(pedidoCancelado);
                    }
                }else{
                    block();
                }
            }
        });
    }
}
