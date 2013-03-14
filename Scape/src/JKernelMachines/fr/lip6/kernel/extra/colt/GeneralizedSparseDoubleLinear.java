package JKernelMachines.fr.lip6.kernel.extra.colt;

import JKernelMachines.fr.lip6.kernel.Kernel;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

public class GeneralizedSparseDoubleLinear extends Kernel<double[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6618610116348247480L;
	
	SparseDoubleMatrix2D M;
	int size;
	
	public GeneralizedSparseDoubleLinear(SparseDoubleMatrix2D innerProduct)
	{
		if(innerProduct.columns() != innerProduct.rows())
		{
			M = null;
			size = 0;
		}
		else
		{
			M = innerProduct;
			size = M.columns();
		}
		
		
	}
	
	@Override
	public double valueOf(double[] t1, double[] t2) {
		
		if(t1.length != size && t2.length != size)
			return 0;
		
		SparseDoubleMatrix1D m1 = new SparseDoubleMatrix1D(t1);
		SparseDoubleMatrix1D m2 = new SparseDoubleMatrix1D(t2);
		
		SparseDoubleMatrix1D r = new SparseDoubleMatrix1D(t1.length);
		
		double sum = 0;
		
		M.zMult(m2, r);
		sum = m1.zDotProduct(r);
		
		return sum;
	}

	@Override
	public double valueOf(double[] t1) {
		
		return valueOf(t1, t1);
	}

}
