g = {gt: "funny"} 

try {
    with(g) {
	throw gt
    }
} catch (e) {
}
