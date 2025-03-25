clc; %borrar ventana de comandos
imtool close all; %cierra todas las figures de imtool
close all; %cierra todas las figure menos las de imtool
clear; %resetea todas las variables
workspace; %muestre todo el espacio de trabajo
%% Ejercicio 4
I = imread('BRAIN_RT.TIF');
imshow(I)
figure;
imhist(I)

%% Ejercicio 5a

% Cargar imagen
I = imread('BRAIN_RT.TIF');
% Usamos un umbral basado en el pico 89 (ajustado a la escala [0, 1])
I_adj = imadjust(I, [0 89/255], [0 1]);  % Mapea de [0, 89] a [0, 255]
% Mostrar la imagen ajustada
figure;
imshow(I_adj);
title('Imagen ajustada');

%% Ejercicio 5b

I = imread("BRAIN_RT.TIF");
I1 = im2double(I);
a=0.5;
c=140/255;
[m,n] = size(I1);
for i=1:m
    for j=1:n
        if I1(i,j) < c
            I2(i,j) = I(i,j) * a;
        else
            I2(i, j) = a * c + (I1(i, j) - c) * (1 - a * c) / (1 - c);
        end
    end
end
figure;
imshow(I2);

%% Ejercicio 5c
I = imread('ngc4024l.tif'); 
imshow(I);
I1 = im2double(I); 

% Definir el intervalo para aumentar el contraste
c1 = 0.4;
c2 = 0.6;
a = 1.5; % Factor de aumento de contraste

% Aplicar la transformación
I2 = I1;
for n = 1:size(I1, 2)
    for m = 1:size(I1, 1)
        if I1(m, n) >= c1 && I1(m, n) <= c2
            I2(m, n) = a * (I1(m, n) - c1) / (c2 - c1);
        end
    end
end

figure, imshow(I2); 

%% Ejercicio 5d
I = imread('ngc4024l.tif'); 
I1 = im2double(I); 

a = 4; % Factor de aumento del contraste
I2 = I1;

% Aplicar la transformación cuadrática
for n = 1:size(I1, 2)
    for m = 1:size(I1, 1)
        I2(m, n) = a * I1(m, n) ^ 2;
        if I2(m, n) > 1
            I2(m, n) = 1;
        end
    end
end

figure, imshow(I2); 


%% Ejercicio 5e
I = imread('ngc4024l.tif'); 
I1 = im2double(I); % Convertir la imagen a valores entre 0 y 1


% Mostrar el histograma para analizar las intensidades
figure, imhist(I1), title('Histograma de la imagen');

% Seleccionar un umbral para los objetos brillantes
umbral = 0.4; % Escogemos este umbral

% Crear imagen binaria
I_bin = I1 > umbral; % Píxeles mayores al umbral se convierten en blancos (1), los demás en negros (0)

figure, imshow(I_bin), title('Imagen Binaria con Objetos Brillantes');





