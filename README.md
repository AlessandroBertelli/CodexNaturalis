# Prova Finale di Ingegneria del Software - A.A. 2023-2024

Implementazione del gioco da tavolo [Codex Naturalis](https://www.craniocreations.it/prodotto/codex-naturalis/).

Il progetto prevede la realizzazione di un sistema distribuito composto da un server capace di gestire una singola partita alla volta e più client (uno per giocatore) che possono partecipare a una sola partita alla volta. Il progetto utilizza il pattern MVC (Model-View-Controller) e la comunicazione in rete è gestita tramite socket e RMI.

Interazione e gameplay: interfaccia a riga di comando (CLI) e grafica (GUI).

## Documentazione

### UML
Il diagramma delle classi seguente rappresenta il modello sviluppato durante la progettazione e realizzazione del progetto:
- [UML](https://github.com/LucaBigatti/IS24-AM23/tree/main/deliveries/uml)
- [Sequence Diagram](https://github.com/LucaBigatti/IS24-AM23/blob/main/deliveries/uml/Sequence%20diagram%20Network.pdf)

### Librerie e Plugin Utilizzati
| Libreria/Plugin | Descrizione |
|-----------------|-------------|
| __Maven__       | Strumento per l'automazione della compilazione di progetti Java. |
| __JavaSwing__   | Libreria grafica per la creazione di interfacce utente. |
| __JUnit__       | Framework per unit testing. |

## Funzionalità Implementate
### Funzionalità Base
- _Set di regole_: Regole complete del gioco
- _Interfaccia grafica_: CLI e GUI
- _Connessioni di rete_: Supporto sia per Socket che per RMI

### Funzionalità Avanzate
- __Chat:__ Implementazione di una chat in-game per permettere ai giocatori di comunicare tra loro durante la partita.
- __Resilienza alle disconnessioni:__ I giocatori che si disconnettono a causa di problemi di rete o crash del client possono ricollegarsi e riprendere la partita. Durante la disconnessione, i turni del giocatore vengono saltati e gli altri giocatori connessi continuano il gioco. In caso di gioco con due player a fronte di una disconnessione di un player il gioco viene sospeso.
## Autori
- Davide Ali
- Alessandro Bacchio
- Alessandro Bertelli
- Luca Bigatti
