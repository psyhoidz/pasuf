#pragma once
#include "Iterator.h"
#include "FixedSizeArray.h"

template<class Item>
class FixedArrayIterator : public Iterator<Item>
{
public:
	FixedArrayIterator(FixedSizeArray<Item>* aArray);

	virtual void First();
	virtual void Next();
	virtual bool IsDone() const;
	virtual Item CurrentItem() const;

private:
	FixedSizeArray<Item> *_array;
	long _current;
};
