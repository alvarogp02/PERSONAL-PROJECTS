% Ejercicio 1
% Comienzo limpiando el entorno
clc; %borrar ventana de comandos
imtool close all; %cierra todas las figures de imtool
close all; %cierra todas las figure menos las de imtool
clear; %resetea todas las variables
workspace; %muestre todo el espacio de trabajo

% Imagen 1/3
% Cargamos la imagen
imgBacteria = imread('bacteria.tif');
figure;
imshow(imgBacteria);
title('Bacteria');

% ¿Cuál es su tamaño?
size(imgBacteria);

% ¿Cuántos niveles de grises puede tener?
class(imgBacteria);

% ¿Cuál es el valor mínimo y máximo?
Min_imgBacteria = min(imgBacteria(:)); % uso ':' para convertir la matriz en un solo vector columna
Max_imgBacteria = max(imgBacteria(:));

% examinar con imtool
imtool(imgBacteria);

% Imagen 2/3
% Cargamos la imagen
imgFlowers = imread('flowers.tif');
figure;
imshow(imgFlowers)
title('Flowers');
% ¿Cuál es su tamaño?
size(imgFlowers);

% ¿Cuál es su clase?
class(imgFlowers);

% ¿Cuál es su valor máximo y mínimo?
max_imgFlowers = max(imgFlowers(:));
min_imgFlowers = min(imgFlowers(:));

% Separar las tres componentes de color
figure;
imshow([imgFlowers(:,:,1), imgFlowers(:,:,2), imgFlowers(:,:,3)]);
title('Componentes RGB de flowers');

% examinar con imtool
imtool(imgFlowers);

% Imagen 3/3
% Cargamos la imagen
imgSaturno = imread('Saturno.tif');
figure;
imshow(imgSaturno);
title('Saturno');
% ¿Cuál es su tamaño?
size(imgSaturno);

% ¿Cuál es su clase?
class(imgSaturno);

% ¿Cuál es su valor máximo y mínimo?
max_imgSaturno = max(imgSaturno(:));
min_imgSaturno = min(imgSaturno(:));

% examinar con imtool
imtool(imgSaturno);

%% 

% Ejercicio 2
imgEntGris = imread('coins.png'); 

% Mostrar imagen original
figure;
imshow(imgEntGris);
title('Imagen original (escala de grises)');
impixelinfo; 

% Convertir imagen binaria
imgBW = im2bw(imgEntGris);

% Mostrar la imagen binaria
figure;
imshow(imgBW);
title('Imagen binaria');
impixelinfo;

% Convertir a imagen indexada una a color
% Utilizare de nuevo 'flowers'
I_color = imread('flowers.tif');

% Especificar el número de colores para la imagen indexada
numColors = 7;

% Convertir la imagen RGB a una imagen indexada
[indexedImage, cmap] = rgb2ind(I_color, numColors);

% Mostrar la imagen indexada con su mapa de colores
figure;
imshow(indexedImage, cmap);
title('Imagen indexada');
colormap(cmap);
colorbar;

% Valores de los píxeles
impixelinfo;

%% 

% Ejercicio 3

% Leer una imagen 2D desde un fichero cuyo nombre es pasado como parámetro
procesarImagen('cameraman.tif');

%%

% Ejercicio 4
% Crear una imagen de 120x200 píxeles
imgBW = false([120, 200]);

% Añadir franjas
for i = 1:40:120
    imgBW(i:i+19, :) = true;
end

% Mostrar la imagen con franjas horizontales
figure(1);
imshow(imgBW);
title('Franjas horizontales');

% Mostrar la imagen con franjas verticales
figure(2);
imshow(imgBW');
title('franjas verticales');

%%
% Ejercicio 5
function construirImagenes()
    % Dimensiones
    filas = 256;
    columnas = 256;
    
    % Crear una imagen con variación del nivel de gris en filas
    imgFilas = repmat((0:255)', 1, columnas);
    
    % Crear una imagen con variación del nivel de gris en las columnas
    imgColumnas = repmat(0:255, filas, 1);
    
    % Visualizar la imagen con variación en filas
    figure(1);
    imshow(uint8(imgFilas)); % Convierto a uint8 para mostrar
    title('Franjas horizontales');
    
    % Visualizar la imagen con variación en columnas
    figure(2);
    imshow(uint8(imgColumnas)); % Convierto a uint8 para mostrar
    title('Franjas verticales');
end

construirImagenes()