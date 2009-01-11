#include "FixedArrayIterator.h"

template<class Item>
FixedArrayIterator<Item>::FixedArrayIterator(FixedSizeArray<Item>* aArray)
	:_array(aArray), _current(0)
{
	;//Nothing
}

template <class Item>
Item FixedArrayIterator<Item>::CurrentItem () const 
{
	if (IsDone()) {
		/*throw IteratorOutOfBounds*/;
	}
	return _array->Get(_current);
}

template<class Item>
void FixedArrayIterator<Item>::First() 
{
	_current = 0;
}

template<class Item>
void FixedArrayIterator<Item>::Next()
{
	_current++;
}

template<class Item>
bool FixedArrayIterator<Item>::IsDone() const
{
	return _current >= _array->Count();
}