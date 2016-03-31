#coding=utf-8

from collections import OrderedDict

def convert_to_adjmatrix(filename):
    vertex_adj = OrderedDict()
    with open(filename) as f:
        for line in f:
            terms = line.split('\t')
            terms = [term.strip() for term in terms]
            if len(terms) != 2:
                continue

            vertex_from, vertex_to = terms[0], terms[1]
            print vertex_from, vertex_to
            if not vertex_adj.has_key(vertex_from):
                vertex_adj[vertex_from] = list()
            vertex_adj[vertex_from].append(vertex_to)
    return vertex_adj


def test(adjMatrix):
    cnt = 0
    for vertex, vertex_list in adjMatrix.iteritems():
        if cnt >= 10:
            break
        print vertex,vertex_list[:5]
        cnt += 1

if __name__ == '__main__':
    vertex_adj = convert_to_adjmatrix('1.txt')
    test(vertex_adj)
