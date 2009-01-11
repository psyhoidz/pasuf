#pragma once

#include "Iterator.h"


template<class Item>
class Agregate
{
public:
	Agregate(void);

	virtual Iterator<Item>& CreateIterator() const = 0;

};
