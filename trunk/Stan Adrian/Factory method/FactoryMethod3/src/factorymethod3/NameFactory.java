
package factorymethod3;

class NameFactory {
//returns o instanta a clasei LastFirst sau FirstFirst
//totul depinzand de existent virgulei

public Namer getNamer(String entry) {
int i = entry.indexOf(","); //virgula determina care clasa
if (i>0)
return new LastFirst(entry); //returneaza istanta unei clase
else
return new FirstFirst(entry); //sau a celeilalte.
}
}