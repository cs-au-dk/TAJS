var name, obj = {}; 
for (name in obj) {
	assert(false); // no enumrable properties in prototype
}