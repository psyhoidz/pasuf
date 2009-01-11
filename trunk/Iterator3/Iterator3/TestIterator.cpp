#include <iostream>
#include "FixedSizeArray.h"

using namespace std;


int main(int argc, char *argv[]) 
{
	FixedSizeArray<int> a;
	Iterator<int> *i;
	cout << "iterator test start:" << endl;
	i = a.CreateIterator();
	a.Append(10);
	a.Append(20);
	a.Append(30);
	for(i->First(); i->IsDone(); i->Next()) {
		cout << i->CurrentItem() << endl;
	}
	cout << "iterator test finish." << endl;
	return 0;
}