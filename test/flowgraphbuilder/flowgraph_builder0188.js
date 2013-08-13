var x,y,z;
l1: for (x = 0; x < 10; x++) {
	l2: for (y = 0; y < 10; y++) {
		l3: for (z = 0; z < 10; z++) {
			var  r = Math.random();
			if (z > 0.1)
				break l1;
			if (z > 0.2)
				break l2;
			if (z > 0.3)
				break l3;
			if (z > 0.4)
				break;
			if (z > 0.1)
				continue l1;
			if (z > 0.2)
				continue l2;
			if (z > 0.3)
				continue l3;
			if (z > 0.4)
				continue;
			r = 42;
		}
		
	}
	
}
