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
public class Estoque extends Agent{
    
    int massaP = 40;
    int molhoT = 60;
    int calab = 16;
    int azeitonaP = 35;
    int ceb = 16;
    
    @Override
    protected void setup(){
        addBehaviour(new CyclicBehaviour(this){
            
            @Override
            public void action(){
                ACLMessage msgProEstoque = myAgent.receive();
                if(msgProEstoque != null){
                    String msgContendoOsIngredientes = msgProEstoque.getContent();
                    
                    ACLMessage respImediataRefAosIngredientesProCozinheiro = msgProEstoque.createReply();
                    respImediataRefAosIngredientesProCozinheiro.setPerformative(ACLMessage.INFORM);
                    respImediataRefAosIngredientesProCozinheiro.setContent("Ingredientes que você precisa recebidos " + msgProEstoque.getSender().getName() + "! Vou verificar se existem suficientes e jaja lhe retorno.");
                    myAgent.send(respImediataRefAosIngredientesProCozinheiro);
                    
                    if("1 Massa de pizza, 1 Molho de tomate, 2 Calabresas, 8 Azeitonas pretas e 1 Cebola.".equals(msgContendoOsIngredientes)){
                        if(massaP >= 1 && molhoT >= 1 && calab >= 2 && azeitonaP >= 8 && ceb >= 1){
                            massaP--;
                            molhoT--;
                            calab = calab -2;
                            azeitonaP = azeitonaP - 8;
                            ceb--;
                            
                            //Env msg pro cozinheiro dizendo q tem os ingredientes!
                            ACLMessage msgPosCozinheiro = new ACLMessage(ACLMessage.INFORM);
                                msgPosCozinheiro.addReceiver(new AID("Caue", AID.ISLOCALNAME));
                                msgPosCozinheiro.setLanguage("Português");
                                msgPosCozinheiro.setOntology("Obrigação");
                                msgPosCozinheiro.setContent("Há ingredientes suficientes!");
                                myAgent.send(msgPosCozinheiro);
                            }else {
                            //Env msg pro cozinheiro dizendo q n tem os ingredientes!
                            ACLMessage msgNegCozinheiro = new ACLMessage(ACLMessage.INFORM);
                                msgNegCozinheiro.addReceiver(new AID("Caue", AID.ISLOCALNAME));
                                msgNegCozinheiro.setLanguage("Português");
                                msgNegCozinheiro.setOntology("Obrigação");
                                msgNegCozinheiro.setContent("Não há ingredientes suficientes!");
                                myAgent.send(msgNegCozinheiro);
                        }
                    }else{
                        //Soup dizendo q os ingredientes passados n são "o programado".
                        System.out.println("Os ingredientes não são os que eu esperava receber.");
                        block();
                    }
                } else{
                    System.out.println("Não recebi os ingredientes, Cozinheiro!");
                    block();
                }
            }
        });
    }
}
