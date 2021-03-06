/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package graphlets;

import java.sql.Connection;
import com.mxgraph.layout.mxCompactTreeLayout;
import java.util.ArrayList;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxStylesheet;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class QueryGUI extends javax.swing.JFrame {
public static int globalInt =1;
public static int global_result_number = 1;
public static int edgeID =0;
public static List<Graph> results = null;
private Importer dataImport; 
  //public static int z =0;
    /**
     * Creates new form QueryGUI
     */
    public QueryGUI() {
        initComponents();
        dataImport = new Importer(); 
        dataImport.Connect();
        dataImport.ImportFile("C:\\Users\\John\\Documents\\GitHub\\graphlets\\graphs\\Graph_6.txt");
        buildGraph(jPanel1);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setMaximumSize(new java.awt.Dimension(72767, 72767));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 372, Short.MAX_VALUE)
        );

        jToggleButton1.setText("Add/Remove Nodes");

        jButton1.setText("Find Solution");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Next Solution");

        jCheckBox1.setLabel("Use Labels");

        jButton3.setText("Clear All");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addGap(87, 87, 87)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(130, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSplitPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QueryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QueryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QueryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QueryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QueryGUI().setVisible(true);
            }
        });
    }
    private void buildGraph(JPanel panel) {
        Graph queryGraph = new Graph();
     // List<Graph> results = null;
	final mxGraph graph = new mxGraph();
	panel.setLayout(new BorderLayout(0, 0));
        //mxConstants.STYLE_SHAPE = mxConstants.SHAPE_ELLIPSE;
       // String Style = mxConstants.STYLE_SHAPE;
        final mxGraphComponent graphComponent = new mxGraphComponent(graph);
	panel.add(graphComponent);
        jButton2.setVisible(false);
        
        //event listener for the "find solution" button
        jButton1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                mxCompactTreeLayout layout = new mxCompactTreeLayout(graph);
                boolean useLabels = false;
                if(jCheckBox1.isSelected())
                    useLabels = true;
                
                final Graph queryGraph = new Graph();
              //  int vertid = 0;
                //all vertices in the GUI graph
                Object[] Qvert = graph.getChildVertices(graph.getDefaultParent());
                //instance of queryIndex
                QueryIndex QI[] = new QueryIndex[50];
                for(int i=0; i <QI.length; i++)
                {
                    //initialize
                    QI[i] = new QueryIndex();
                }
                int idx = 0;
                for(Object Q : Qvert)
                {
                 //load QueryIndex with an ID, label and the vertex from the GUI
                 QI[idx].VertexIndex = idx+1;
                 QI[idx].Vertice = Q;
                 QI[idx].NodeLabel = graph.getLabel(Q);
                 idx++;
                }
                jButton2.setVisible(true);//the Next solution button
               //get Edges
                for(int i =0; i <QI.length; i++)
                {
                    Object []outgoing = graph.getOutgoingEdges(QI[i].Vertice );
                    for(Object out : outgoing)
                    {
                         for(int j =0; j <QI.length; j++)
                         {
                             Object [] incoming = graph.getIncomingEdges(QI[j].Vertice);
                             for(Object in : incoming)
                             {
                                 if((out == in)&& (QI[i].VertexIndex != QI[j].VertexIndex))
                                 {
                                      queryGraph.ItsEdges.add(new Edge(edgeID, QI[i].VertexIndex, QI[j].VertexIndex, QI[i].NodeLabel+"_"+QI[j].NodeLabel));
                                      edgeID++;
                                     
                                 }
                                     
                             }
                         }
                    }
                    if(QI[i].VertexIndex != 0)
                        queryGraph.ItsVertices.add(new Node(QI[i].VertexIndex , QI[i].NodeLabel));
                }
                
                //retrives the results of the matching structure within the database
                Matcher m = new Matcher(dataImport.ItsConnection);
                results = m.GetMatchingGraphs(queryGraph, useLabels);
                if(results == null)
                    System.out.println("no results");
                boolean init = true;
                int idxx = 0;
                for(Graph g_load : results)
                {
                     if(init == true)//limit to the first graph result
                     {
                        // System.out.println("should print");
                         graph.selectAll();
                         graph.removeCells();
                         for(Node n :  g_load.ItsVertices)
                         {
                             graph.insertVertex(graph.getDefaultParent(), null, n.ItsLabel, 0, 0, 80, 40,"shape=ellipse");
                         }
                         //Newgraph is the graph on the screen
                         Object[] NewGraph = graph.getChildVertices(graph.getDefaultParent());
                          //resultidx will be the loaded class with info from the results and Newgraph
                         List<QueryIndex> resultidx = new ArrayList<>();
                         for(int i=0; i <NewGraph.length; i++)
                         {
                             //initialize
                             resultidx.add(new QueryIndex());
                         }
                         
                         for(Node n :  g_load.ItsVertices)//load the nodes into QueryIndex
                         {
                             for(Object nwgrph : NewGraph)
                             {
                                 if(graph.getLabel(nwgrph).equals( n.ItsLabel))
                                 {
                                     resultidx.get(idxx).VertexIndex = n.ItsVertexId;
                                     resultidx.get(idxx).Vertice = nwgrph;
                                     resultidx.get(idxx).NodeLabel = graph.getLabel(nwgrph);
                                     idxx++;
                                 }
                             }
                         }
                         //edges for Find Solution
                      //   for(QueryIndex check : resultidx)
                       //  {
                       //      System.out.println(check.VertexIndex);
                      //   }
                         for(Edge newedge: g_load.ItsEdges)
                         {
                             for(int eidx = 0; eidx < resultidx.size(); eidx++)
                             {
                                 if(resultidx.get(eidx).VertexIndex == newedge.ItsFirstNode)
                                 {
                                      for(int eidx2 = 0; eidx2 < resultidx.size(); eidx2++)
                                      {
                                          if(resultidx.get(eidx2).VertexIndex == newedge.ItsSecondNode)
                                          {
                                            //  System.out.println("adding edge");
                                              graph.insertEdge(graph.getDefaultParent(), null, newedge.ItsLabel, resultidx.get(eidx).Vertice, resultidx.get(eidx2).Vertice,"startArrow=none;endArrow=none;strokeWidth=2;strokeColor=#6CADCB");
                                              layout.execute(graph.getDefaultParent());
                                          }
                                      }
                                 }
                             }
                         }
                    }
                     
                   init = false;//a single graph result   
                 }                                          
            }
        });//end of find solutions
        
		//Next solution (jbutton2)
        
        jButton2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            { 
               // int result_number = 0;//allows us to loop through the list of graph results
                Graph graphResult = results.get(global_result_number);//the Next button will start at the next result in the list which was returned by the graphMatcher
                {
                    if(global_result_number < 100)//loop through the results till you get to the next one
                    {
                         graph.selectAll();
                         graph.removeCells();
                         for(Node n :  graphResult.ItsVertices)
                         {
                             graph.insertVertex(graph.getDefaultParent(), null, n.ItsLabel, 0, 0, 80, 40,"shape=ellipse");
                         }
                         
                         Object[] NewGraph = graph.getChildVertices(graph.getDefaultParent());
                          //after the verts have been drawn, capture the graph on the screen into the class format
                         List<QueryIndex> resultidx = new ArrayList<>();
                         for(int i=0; i <NewGraph.length; i++)
                         {
                             //initialize
                             resultidx.add(new QueryIndex());
                         }
                         int idxx=0;
                         for(Node n :  graphResult.ItsVertices)//load the nodes into QueryIndex
                         {
                             for(Object nwgrph : NewGraph)
                             {
                                 if(graph.getLabel(nwgrph).equals( n.ItsLabel))
                                 {
                                     resultidx.get(idxx).VertexIndex = n.ItsVertexId;
                                     resultidx.get(idxx).Vertice = nwgrph;
                                     resultidx.get(idxx).NodeLabel = graph.getLabel(nwgrph);
                                     idxx++;
                                 }
                             }
                         }
                         //edges for Next Solution
                         for(Edge newedge : graphResult.ItsEdges)
                         {
                             for(int eidx = 0; eidx < resultidx.size(); eidx++)
                             {
                                 if(resultidx.get(eidx).VertexIndex == newedge.ItsFirstNode)
                                 {
                                      for(int eidx2 = 0; eidx2 < resultidx.size(); eidx2++)
                                      {
                                          if(resultidx.get(eidx2).VertexIndex == newedge.ItsSecondNode)
                                             graph.insertEdge(graph.getDefaultParent(), null, newedge.ItsLabel, resultidx.get(eidx).Vertice, resultidx.get(eidx2).Vertice,"startArrow=none;endArrow=none;strokeWidth=2;strokeColor=#6CADCB"); 
                                      }
                                 }
                             }
                         }

                         mxCompactTreeLayout layout = new mxCompactTreeLayout(graph);
                             // layout.set
                         layout.execute(graph.getDefaultParent());
                        global_result_number++;//the number of the next result we'll want to view
                    }
                  //  result_number++;//how many results we've looped through
                }
                
            }
        });//end of Next Solution
        //Clear ALL
        jButton3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            { 
                         graph.selectAll();
                         graph.removeCells();
                         globalInt = 1;
            }
        });
        
        //add/remove button(togglebutton1)
	graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
 
                public void mouseReleased(MouseEvent e) {
                        if (jToggleButton1.isSelected()) {
                                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                                
                                if (cell == null)
                                {
                                    graph.insertVertex(graph.getDefaultParent(), null, globalInt, e.getX(), e.getY(), 80, 40,"shape=ellipse");
                                    globalInt++;
                               // System.out.println( graph.getLabel(cell));
                                 
                                }
                                else
                                {
                                    Object[] edges = graph.getEdges(cell);
                                    for(Object edge:edges)
                                        graph.getModel().remove(edge);
                                   
                                    graph.getModel().remove(cell);
                                }
                        }

                }
        });
	}


    /////////////////
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
