#pragma once
#include "Iterator.h"
//#include "FixedArrayIterator.h"

#define DEFAULT_LIST_CAPACITY 100

template<class Item>
class FixedSizeArray
{
protected:
	Item* _array;
	long _size, _nrItems;

public:
	FixedSizeArray(long size = DEFAULT_LIST_CAPACITY);

	Item& Get(long index) const;

	void Append(Item x);
	void Remove(long index);
	long Count() const;
	Iterator<Item>* CreateIterator() const;
};
