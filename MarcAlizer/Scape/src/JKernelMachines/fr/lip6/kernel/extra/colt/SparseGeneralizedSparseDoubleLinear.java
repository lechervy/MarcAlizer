package JKernelMachines.fr.lip6.kernel.extra.colt;

import JKernelMachines.fr.lip6.kernel.Kernel;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

public class SparseGeneralizedSparseDoubleLinear extends Kernel<SparseDoubleMatrix1D> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2492610095559764783L;
	
	SparseDoubleMatrix2D M;
	int size;
	
	public SparseGeneralizedSparseDoubleLinear(SparseDoubleMatrix2D innerProduct)
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
	public double valueOf(SparseDoubleMatrix1D t1, SparseDoubleMatrix1D t2) {
		
		if(t1.size() != size && t2.size() != size)
			return 0;
		
		SparseDoubleMatrix1D r = new SparseDoubleMatrix1D(t1.size());
		
		double sum = 0;
		
		M.zMult(t2, r);
		sum = t1.zDotProduct(r);
		
		return sum;
	}

	@Override
	public double valueOf(SparseDoubleMatrix1D t1) {
		
		return valueOf(t1, t1);
	}

}
