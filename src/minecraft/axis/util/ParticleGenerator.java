package axis.util;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.util.MathHelper;

public class ParticleGenerator {
	private int count;
	private int width;
	private int height;
	private ArrayList<Particle> particles = new ArrayList();
	private Random random = new Random();
	private TimeHelper timer = new TimeHelper();
	int state = 0;
	int a = 255;
	int r = 255;
	int g = 0;
	int b = 0;

	public ParticleGenerator(int count, int width, int height) {
		this.count = count;
		this.width = width;
		this.height = height;
		for (int i = 0; i < count; i++) {
			this.particles.add(new Particle(this.random.nextInt(width), this.random.nextInt(height)));
		}
	}

	public void drawParticles() {
		for (Particle p : this.particles) {
			if (p.reset) {
				p.resetPosSize();
				p.reset = false;
			}
			p.draw();
		}
	}

	public class Particle {
		private int x;
		private int y;
		private int k;
		private float size;
		private boolean reset;
		private Random random = new Random();
		private TimeHelper timer = new TimeHelper();

		public Particle(int x, int y) {
			this.x = x;
			this.y = y;
			this.size = genRandom(1.0F, 3.0F);
		}

		public void draw() {
			if (this.size <= 0.0F) {
				this.reset = true;
			}
			this.size -= 0.05F;
			this.k += 1;
			int xx = (int) (MathHelper.cos(0.1F * (this.x + this.k)) * 10.0F);
			int yy = (int) (MathHelper.cos(0.1F * (this.y + this.k)) * 10.0F);
			RenderUtils.drawBorderedCircle(this.x + xx, this.y + yy, this.size, 0, 553648127);
		}

		public void resetPosSize() {
			this.x = this.random.nextInt(ParticleGenerator.this.width);
			this.y = this.random.nextInt(ParticleGenerator.this.height);
			this.size = genRandom(1.0F, 3.0F);
		}

		public float genRandom(float min, float max) {
			return (float) (min + Math.random() * (max - min + 1.0F));
		}
	}
}
