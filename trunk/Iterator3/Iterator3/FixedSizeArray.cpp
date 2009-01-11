#include "FixedSizeArray.h"
#include "FixedArrayIterator.h"
#include <memory.h>

template<class Item>
FixedSizeArray<Item>::FixedSizeArray(long size) 
	:_size(size), _nrItems(0)
{
	_array = new Item[size];
	memset(_array, 0, size);
}

template<class Item>
Item& FixedSizeArray<Item>::Get(long index) const
{
	return _array[index];
}

template<class Item>
void FixedSizeArray<Item>::Append(Item x)
{
	if(_nrItems < _size)
		_array[_nrItems++] = x;
}

template<class Item>
void FixedSizeArray<Item>::Remove(long index)
{
}

template<class Item>
long FixedSizeArray<Item>::Count() const
{
	return this->_nrItems;
}

template<class Item>
Iterator<Item>* FixedSizeArray<Item>::CreateIterator() const
{
	return new FixedArrayIterator<Item>(this);
}