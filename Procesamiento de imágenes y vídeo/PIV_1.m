clc; %borrar ventana de comandos
imtool close all; %cierra todas las figures de imtool
close all; %cierra todas las figure menos las de imtool
clear; %resetea todas las variables
workspace; %muestre todo el espacio de trabajo
%% Ejercicio 1a
% Coordenadas del foco (cambiar posición y valores)

a = 100;  
b = 200;  

% Dimensiones de la imagen (MxN)
M = 400; 
N = 400; 

% Construcción de la imagen
for x = 1:M
    for y = 1:N
        I(x, y) = (255 - sqrt((x - a)^2 + (y - b)^2)) / 255;
    end
end

imshow(I);
title('Imagen creada con nuevos parámetros');
%% Ejercicio 1b

I = ones(50, 50);

I(15:34, 21:30) = 0;

% Visualizar la imagen
imshow(I, 'InitialMagnification', 'fit');
title('Rectángulo negro');
%% Ejercicio 1c
I = imread('Imágenes/aerea.jpg'); 

% Extraer la región de interés
I_ROI = imcrop(I, [50 30 120 100]);
% Mostrar la imagen original y la ROI
figure(1)
imshow(I);
title('Imagen original');
figure(2);
imshow(I_ROI);
title('Región de interés extraída');
%% Ejercicio 1c a color
I = imread('Imágenes/flowers.tif'); 

% Extraer la región de interés
I_ROI = imcrop(I, [50 30 120 100]);
% Mostrar la imagen original y la ROI
figure(1)
imshow(I);
title('Imagen original');
figure(2);
imshow(I_ROI);
title('Región de interés extraída');

%% Ejercicio 2a

I = imread('Imágenes/Galaxia.jpg'); 
I_mult = I * 1.5; % Multiplicación por 1.5
I_sum = I + 40;   % Suma de 40 

figure(2);
imshow(I_mult), title('Imagen multiplicada')
figure(3);
imshow(I_sum), title('Imagen con suma')

%% Ejercicio 2b

I = imread('Imágenes/Galaxia.jpg'); 
imshow(I);
zoom on; % Activar zoom manualmente

%% Ejercicio 2c
I = imread('Imágenes/Galaxia.jpg'); 
J = imresize(I, 0.6, 'lanczos2'); 
imshow(J);
title("Reducción al 60%");

J1 = imresize(I, [50 70]); 
figure;
imshow(J1);
title("Reducción a 50x70");
 
%% Ejercicio 2d
IR=imrotate(J,45); 
figure;
imshow(IR);
title("Rotada 45º");
function rotatedImage = rotate90(I)
    % Gira la imagen 90º
    rotatedImage = rot90(I); 
end
rotated = rotate90(J);
figure;
imshow(rotated);
title("Rotada 90º")

%% Ejercicio 3
I = imread('text.tif');  
c = [120 30];  % posición de columna
r = [180 80];  % posición de fila
I0 = bwselect(I, c, r, 8); 

imshow(I);
figure, imshow(I0);

% Obtener las coordenadas de los píxeles
[selectedRows, selectedCols] = find(I0);  

% Calcular Bounding Box (esquinas)
minRow = min(selectedRows);
maxRow = max(selectedRows);
minCol = min(selectedCols);
maxCol = max(selectedCols);

% Mostrar Bounding Box
disp(['Bounding Box: (' num2str(minRow) ', ' num2str(minCol) ') a (' num2str(maxRow) ', ' ...
    num2str(maxCol) ')']);