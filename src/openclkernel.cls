
_kernel void accelerate(int const size, _global const float* posx, _global const float* posy,_global const float* posz, _global float* velx, _global float* vely, _global float* velz, _global const float* mass) {
	const int itemId = get_global_id(0); 

	if(itemId < size) {
		//for(int i=0; i< size; i++){
			if(i == itemId) continue;
			float acceleration, deltax, deltay, deltaz, distance;
			deltax = posx[itemId] - posx[i];
			deltay = posy[itemId] - posy[i];
			deltaz = posz[itemId] - posz[i];
			distance = rsqrt(pown(deltax,2)+pown(deltay,2)+pown(deltaz,2));
			acceleration =(mass[i] * distance)/mass[p];
			acceleration /= 10000000f;
			velx[itemId] -= (deltax*distance) * acceleration;
			vely[itemId] -= (deltay*distance) * acceleration;
			velz[itemId] -= (deltaz*distance) * acceleration;
		}
	}
}