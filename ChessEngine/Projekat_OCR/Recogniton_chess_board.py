
# coding: utf-8

# In[9]:

#get_ipython().magic(u'matplotlib inline')
import sys
import cv2
import numpy as np
import json
from matplotlib import pyplot as plt
import matplotlib.pylab as pylab
pylab.rcParams['figure.figsize'] = 16, 12 # za prikaz većih slika i plotova, zakomentarisati ako nije potrebno

def display_image(image, color= False):
    if color:
        plt.imshow(image)
    else:
        plt.imshow(image, 'gray')

img_rgb = cv2.imread(sys.argv[1])
img_gray = cv2.cvtColor(img_rgb, cv2.COLOR_BGR2GRAY)

img_rgb_board = cv2.imread('board.jpg',0)

templates = []

pawn_whiteA = cv2.imread('templates/pawn_whiteA.jpg',0)
pawn_whiteB = cv2.imread('templates/pawn_whiteB.jpg',0)
bishop_white = cv2.imread('templates/bishop_white.jpg',0)
knight_white = cv2.imread('templates/knight_white.jpg',0)
rook_white = cv2.imread('templates/rook_white.jpg',0)
queen_white = cv2.imread('templates/queen_white.jpg',0)
king_white = cv2.imread('templates/king_white.jpg',0)

pawn_black = cv2.imread('templates/pawn_black.jpg',0)
bishop_black = cv2.imread('templates/bishop_black.jpg',0)
knight_black = cv2.imread('templates/knight_black.jpg',0)
rook_black = cv2.imread('templates/rook_black.jpg',0)
queen_black = cv2.imread('templates/queen_black.jpg',0)
king_black = cv2.imread('templates/king_black.jpg',0)


templates.append([pawn_whiteA,'P'])
templates.append([pawn_whiteB,'P'])
templates.append([bishop_white,'B'])
templates.append([knight_white,'N'])
templates.append([rook_white,'R'])
templates.append([queen_white,'Q'])

templates.append([king_white,'K'])


templates.append([pawn_black,'p'])
templates.append([bishop_black,'b'])
templates.append([knight_black,'n'])
templates.append([rook_black,'r'])
templates.append([queen_black,'q'])

templates.append([king_black,'k'])

chessboard=[[' ',' ',' ',' ',' ',' ',' ',' '],
           [' ',' ',' ',' ',' ',' ',' ',' '],
           [' ',' ',' ',' ',' ',' ',' ',' '],
           [' ',' ',' ',' ',' ',' ',' ',' '],
           [' ',' ',' ',' ',' ',' ',' ',' '],
           [' ',' ',' ',' ',' ',' ',' ',' '],
           [' ',' ',' ',' ',' ',' ',' ',' '],
           [' ',' ',' ',' ',' ',' ',' ',' ']]


# In[10]:

def select_roi(img_rgb,img_gray,img_board,templates):
    temp_count = 0
    w_orig, h_orig = img_rgb.shape[0], img_rgb.shape[1]
    
    wb, hb = img_board.shape[0], img_board.shape[1]
    
    res_b = cv2.matchTemplate(img_gray,img_board,cv2.TM_CCOEFF_NORMED)
    threshold_b = 0.25
    
    loc_b = np.where( res_b >= threshold_b)
    board_x = 0
    board_y = 0
    for ptb in zip(*loc_b[::-1]):
        cv2.rectangle(img_rgb, ptb, (ptb[0] + wb, ptb[1] + hb), (255,0,0), 1)
        board_x = ptb[0]
        board_y = ptb[1]
        
    for i, template in enumerate(templates):
        w, h = template[0].shape[::-1]
        res = cv2.matchTemplate(img_gray,template[0],cv2.TM_CCOEFF_NORMED)
        threshold = 0.948
    
        loc = np.where( res >= threshold)
        
        for pt in zip(*loc[::-1]):
            cv2.rectangle(img_rgb, pt, (pt[0] + w, pt[1] + h), (0,0,255), 1)
            #print pt[0] , pt[1]
            centerx = pt[0] + w/2
            centery = pt[1] + h/2
            r = -1
            c = -1
            if centerx <= wb/8 + board_x:
                c = 1
            if centery <= hb/8 + board_y:
                r = 1
            for i in range(2,9):
                if centerx <= i*wb/8.0 + board_x and centerx >=(i-1)*wb/8.0 + board_x:
                    c = i
            for j in range(2,9):                
                if centery <= j*hb/8.0 + board_y and centery >= (j-1)*hb/8.0 + board_y:
                    r = j
            if (r != -1 and c != -1):
                chessboard[r-1][c-1] = template[1]
            
            temp_count+=1
            
    #cv2.imwrite('res.png',img_rgb)
    #print chessboard
    return img_rgb , temp_count , chessboard


# In[11]:

image_selected , temp_count, chessboard =  select_roi(img_rgb,img_gray,img_rgb_board,templates)
#print temp_count
#print ""
#print chessboard
#display_image(image_selected)

outFile = open('output1.json', 'w')
json.dump(chessboard,outFile ,indent=1)
outFile.close()


# In[ ]:




# In[ ]:



