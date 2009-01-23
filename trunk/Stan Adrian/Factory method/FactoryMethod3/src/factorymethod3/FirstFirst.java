/*
 Acuma putem scrie doua foarte simple clase care imparte numele in 2 parti.
 In clasa Firstfirst presupunem ca tot ce este inaintea spatiului 
 este prenumele.
 */

package factorymethod3;

class FirstFirst extends Namer { //impartim formatul prenume nume
public FirstFirst(String s) {
int i = s.lastIndexOf(" "); //gasim separatorul spatiu
if (i > 0) {
//in stanga se afla prenumele
first = s.substring(0, i).trim();
//in dreapta se afla numele
last =s.substring(i+1).trim();
}
else {
first = "";// se pune totul in prenume
last = s; // in cazul in care nu avem spatiu

}
}
}