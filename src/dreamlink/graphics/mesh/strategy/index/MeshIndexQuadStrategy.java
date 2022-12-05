package dreamlink.graphics.mesh.strategy.index;

public class MeshIndexQuadStrategy {

    private static int[] quadIndices = new int[] { 
        0, 1, 3, 3, 1, 2 
    };

    private IMeshIndexStrategyProvider provider;

    public MeshIndexQuadStrategy(IMeshIndexStrategyProvider provider) {
        this.provider = provider;
    }

    public void add() {
        var buffer = this.provider.getIndexBuffer();
        var numQuads = buffer.getDataCount() / 6;
        var numVertices = numQuads * 4;
        for(var index : quadIndices) {
            buffer.add(numVertices + index);
        }
    }
    
}
