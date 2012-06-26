import java.io.Serializable;


public class Vec3 implements Serializable{
	public float v1,v2,v3;
	public Vec3() {
		v1 = 0;
		v2 = 0;
		v3 = 0;
	}
	public Vec3(float v1, float v2, float v3){
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}
}
