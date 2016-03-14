#coding=utf8
'''
Created on Mar 14, 2016

@author: myue
'''

from collections import defaultdict

def pageRankMain(adjMatrix, vertexPR, alpha, iters):
    for i in xrange(iters):
        newVertexPR = pageRank(adjMatrix,vertexPR,alpha)
        vertexPR = newVertexPR
        _print(vertexPR,False)

def pageRank(adjMatrix, vertexPR, alpha):
    '''Main Process

    Parameters
    ----------

    adjMatrix: defaultDict(list), Graph-like data structure

    vertexPR: Dict, PageRank value for each vertex

    alpha: float, escape probability

    Returns
    -------

    newVertexPR: Dict, new PageRank value after one iteration
    '''

    newVertexPR = defaultdict(float)
    #map and reduce
    for vertex, edges in adjMatrix.iteritems():
        emittedPR = vertexPR[vertex]/len(edges)
        for vertexTo in edges:
            newVertexPR[vertexTo] += emittedPR
        newVertexPR[vertex] += 0.0

    for vertex in newVertexPR.iterkeys():
        newVertexPR[vertex] = (1 - alpha) * newVertexPR[vertex] + alpha*1.0/len(newVertexPR)

    return newVertexPR

def _error(oldPR, newPR):
    '''compute change over old and new pagerank values

    Parameters
    ----------

    oldPR/newPR: Dict, <vertexID, PageRank>

    Returns
    -------

    error: float
    '''
    error = sum([newPR[vertex] - oldPR[vertex] for vertex in newPR.keys()])
    return error

def _print(vertexPR, flag):
    '''Debug print function

    Parameters
    ----------

    flag: True, print vertex name; False, print pagerank value
    '''
    if flag:
        print '\t'.join([str(name) for name in vertexPR.keys()]) + '\n'
    else:
        print '\t'.join([str(value) for value in vertexPR.values()]) + '\n'


if __name__ == '__main__':
    adjMatrix = {'A':['B','C','D'],
                 'B':['A','D'],
                 'C':['C'],
                 'D':['B','C']
                }
    vertexPR = {'A':0.25,'B':0.25,'C':0.25,'D':0.25}
    alpha = 0.2
    iters = 20

    _print(vertexPR, True)
    _print(vertexPR, False)
    pageRankMain(adjMatrix,vertexPR,alpha,iters)
