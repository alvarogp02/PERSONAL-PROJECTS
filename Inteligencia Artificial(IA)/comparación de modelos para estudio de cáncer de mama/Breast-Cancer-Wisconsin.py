import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import GaussianNB
from sklearn.preprocessing import LabelEncoder
from sklearn.neighbors import KNeighborsClassifier
import matplotlib.pyplot as plt
from sklearn.model_selection import StratifiedKFold, cross_val_score
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score, roc_curve, precision_score, recall_score, f1_score, confusion_matrix, roc_auc_score
from sklearn.model_selection import GridSearchCV
from sklearn.tree import plot_tree
from sklearn.svm import SVC 

# Especifica la ruta del archivo descargado
ruta_archivo = "wdbc.data"

# Definir los nombres de las columnas
nombres_columnas = ['ID', 'Diagnosis','radius_mean','texture_mean','perimeter_mean','area_mean','smoothness_mean','compactness_mean','concavity_mean','concave_points_mean',
                    'symmetry_mean','fractal_dimension_mean','radius_se','texture_se','perimeter_se','area_se','smoothness_se','compactness_se','concavity_se',
                    'concave_points_se','symmetry_se','fractal_dimension_se','radius_worst','texture_worst','perimeter_worst','area_worst','smoothness_worst','compactness_worst',
                    'concavity_worst','concave_points_worst','symmetry_worst','fractal_dimension_worst'
                    ]

# Cargar el archivo CSV en un DataFrame de Pandas
df = pd.read_csv(ruta_archivo, names=nombres_columnas, sep=",", header=None)

# Eliminar la columna 'ID'
df = df.drop(['ID'], axis=1)

# Diagnosis tiene valores 'String', usamos labelEncoder() para asignar 1 a M y 0 a B
le = LabelEncoder()
df['Diagnosis'] = le.fit_transform(df['Diagnosis'])

caracteristicas_columnas = ['radius_mean','texture_mean','perimeter_mean','area_mean','smoothness_mean','compactness_mean','concavity_mean','concave_points_mean',
                    'symmetry_mean','fractal_dimension_mean','radius_se','texture_se','perimeter_se','area_se','smoothness_se','compactness_se','concavity_se',
                    'concave_points_se','symmetry_se','fractal_dimension_se','radius_worst','texture_worst','perimeter_worst','area_worst','smoothness_worst','compactness_worst',
                    'concavity_worst','concave_points_worst','symmetry_worst','fractal_dimension_worst'
                    ]

X = df[caracteristicas_columnas]
y = df['Diagnosis']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.5, random_state=0)


# Inicializamos diccionario para almacenar métricas
metrics_dict = {}






# Clasificador Naive Bayes
estimador_gnb = GaussianNB()
y_pred_train = estimador_gnb.fit(X_train, y_train).predict(X_train)
y_pred_test = estimador_gnb.predict(X_test)
accuracy_train = accuracy_score(y_train, y_pred_train)
accuracy_test = accuracy_score(y_test, y_pred_test)
precision_train = precision_score(y_train, y_pred_train)
precision_test = precision_score(y_test, y_pred_test)
recall_train = recall_score(y_train, y_pred_train)
recall_test = recall_score(y_test, y_pred_test)
f1_train = f1_score(y_train, y_pred_train)
f1_test = f1_score(y_test, y_pred_test)
metrics_dict['GaussianNB'] = {'Train Accuracy': accuracy_train, 'Test Accuracy': accuracy_test, 'Train Precision': precision_train, 'Test Precision': precision_test, 'Train Recall': recall_train, 'Test Recall': recall_test, 'Train F1-Score': f1_train, 'Test F1-Score': f1_test}
print("\nClasificador Naive Bayes")
print("Exactitud(train): ", accuracy_train)
print("Exactitud(test): ", accuracy_test)
print("Precision(train): ", precision_train)
print("Precision(test): ", precision_test)
print("Recall(train): ", recall_train)
print("Recall(test): ", recall_test)
print("F1-Score(train): ", f1_train)
print("F1-Score(test): ", f1_test)









# Clasificador Nearest Neighbors
estimador_neigh = KNeighborsClassifier(n_neighbors=5)
estimador_neigh.fit(X_train,y_train)

#print(estimador_neigh.predict(X_test),'\n')


predict_proba_df = pd.DataFrame(X_test, columns=caracteristicas_columnas)
#print(estimador_neigh.predict_proba(predict_proba_df))

# Definimos lista de números de vecinos a probar
neighbors_list = range(1, 31)
cv_NN = []
stratified_kfold = StratifiedKFold(n_splits=10, shuffle=True, random_state=30)

# Realizar la validación cruzada estratificada 10-fold para cada número de vecinos
for k in neighbors_list:
    kn = KNeighborsClassifier(n_neighbors=k)
    res = cross_val_score(kn, X_train, y_train, cv=stratified_kfold, scoring='accuracy')
    cv_NN.append(res.mean())

# Encontrar el mejor número de vecinos a partir de mayor valor de cv
bestK = neighbors_list[np.argmax(cv_NN)]
print("\nClasificador Nearest Neighbors")
print("El mejor número de vecinos es:", bestK)

# Creamos gráfica
plt.figure(figsize=(10, 10))
plt.plot(neighbors_list, cv_NN)
plt.title('Precisión(media) vs. Número de Vecinos')
plt.xlabel('Número de Vecinos')
plt.ylabel('Precisión')
plt.xticks(neighbors_list)
plt.grid(True)
plt.show()

# Calcular las métricas adicionales
y_pred_train = estimador_neigh.predict(X_train)
y_pred_test = estimador_neigh.predict(X_test)
accuracy_train = accuracy_score(y_train, y_pred_train)
accuracy_test = accuracy_score(y_test, y_pred_test)
precision_train = precision_score(y_train, y_pred_train)
precision_test = precision_score(y_test, y_pred_test)
recall_train = recall_score(y_train, y_pred_train)
recall_test = recall_score(y_test, y_pred_test)
f1_train = f1_score(y_train, y_pred_train)
f1_test = f1_score(y_test, y_pred_test)
print("Exactitud(train):", accuracy_train)
print("Exactitud(test):", accuracy_test)
print("Precision(train):", precision_train)
print("Precision(test):", precision_test)
print("Recall(train):", recall_train)
print("Recall(test):", recall_test)
print("F1-Score(train):", f1_train)
print("F1-Score(test):", f1_test)

# Guardar las métricas en el diccionario
metrics_dict['KNeighborsClassifier'] = {'Train Accuracy': accuracy_train, 'Test Accuracy': accuracy_test, 'Train Precision': precision_train, 'Test Precision': precision_test, 'Train Recall': recall_train, 'Test Recall': recall_test, 'Train F1-Score': f1_train, 'Test F1-Score': f1_test}









# Clasificador de árbol de decisión
estimador_arbol = DecisionTreeClassifier(max_depth=6, min_samples_leaf=10, min_samples_split=3, random_state=30)

# Entrenar el clasificador
estimador_arbol.fit(X_train, y_train)

# Predecir las etiquetas de los datos de entrenamiento y prueba
y_pred_train = estimador_arbol.predict(X_train)
y_pred_test = estimador_arbol.predict(X_test)

# Calcular la precisión para los datos de entrenamiento y prueba
accuracy_train = accuracy_score(y_train, y_pred_train)
accuracy_test = accuracy_score(y_test, y_pred_test)

# Calcular las métricas adicionales para los datos de entrenamiento y prueba
precision_train = precision_score(y_train, y_pred_train)
precision_test = precision_score(y_test, y_pred_test)
recall_train = recall_score(y_train, y_pred_train)
recall_test = recall_score(y_test, y_pred_test)
f1_train = f1_score(y_train, y_pred_train)
f1_test = f1_score(y_test, y_pred_test)
print("\nClasificador de árbol de decisión")
print("Exactitud(train): ", accuracy_train)
print("Exactitud(test): ", accuracy_test)
print("Precision(train): ", precision_train)
print("Precision(test): ", precision_test)
print("Recall(train): ", recall_train)
print("Recall(test): ", recall_test)
print("F1-Score(train): ", f1_train)
print("F1-Score(test): ", f1_test, "\n")

# max_depth es la profundidad máxima del árbol
# min_samples_split es el número mínimo de muestras necesarias para dividir un nodo interno
# min_samples_leaf es el número mínimo de muestras necesarias para estar en un nodo hoja(dejo provisional a 4 sera 12)
param_grid = {'max_depth': range(1, 10),
              'min_samples_split': range(2, 10),
              'min_samples_leaf': range(1, 10)}

# Creo el objeto GridSearchCV
grid_searchcv = GridSearchCV(estimador_arbol, param_grid, cv=10)
grid_searchcv.fit(X, y)

# Imprimir los mejores hiperparámetros
print("Parámetros ideales para árbol: ", grid_searchcv.best_params_)

# Obtener el mejor modelo de GridSearchCV y hacer gráfica
best_decision_tree = grid_searchcv.best_estimator_
plt.figure(figsize=(12, 10))
plot_tree(best_decision_tree, feature_names=caracteristicas_columnas, class_names=['Benigno', 'Maligno'], filled=True)
plt.show()

# Guardar las métricas en el diccionario
metrics_dict['DecisionTreeClassifier'] = {'Train Accuracy': accuracy_train, 'Test Accuracy': accuracy_test, 'Train Precision': precision_train, 'Test Precision': precision_test, 'Train Recall': recall_train, 'Test Recall': recall_test, 'Train F1-Score': f1_train, 'Test F1-Score': f1_test}









# Clasificador Support Vector Machine
estimador_svc = SVC()

# Entrenar el clasificador
estimador_svc.fit(X_train, y_train)

# Predecir las etiquetas de los datos de entrenamiento y prueba
y_pred_train = estimador_svc.predict(X_train)
y_pred_test = estimador_svc.predict(X_test)

# Calcular la precisión para los datos de entrenamiento y prueba
accuracy_train = accuracy_score(y_train, y_pred_train)
accuracy_test = accuracy_score(y_test, y_pred_test)

# Calcular las métricas adicionales para los datos de entrenamiento y prueba
precision_train = precision_score(y_train, y_pred_train)
precision_test = precision_score(y_test, y_pred_test)
recall_train = recall_score(y_train, y_pred_train)
recall_test = recall_score(y_test, y_pred_test)
f1_train = f1_score(y_train, y_pred_train)
f1_test = f1_score(y_test, y_pred_test)
print("\nClasificador Support Vector Machine")
print("Exactitud(train): ", accuracy_train)
print("Exactitud(test): ", accuracy_test)
print("Precision(train): ", precision_train)
print("Precision(test): ", precision_test)
print("Recall(train): ", recall_train)
print("Recall(test): ", recall_test)
print("F1-Score(train): ", f1_train)
print("F1-Score(test): ", f1_test)

# Definir los parámetros para la búsqueda en malla
param_grid = {'C': [0.1, 1], 
              'gamma': [1, 0.1],
              'kernel': ['rbf', 'linear', 'sigmoid']}

# Crear el objeto GridSearchCV con validación cruzada estratificada
grid_search = GridSearchCV(SVC(), param_grid, cv=StratifiedKFold(n_splits=10, shuffle=True, random_state=30))

# Ajustar el modelo a los datos de entrenamiento
grid_search.fit(X_train, y_train)

# Imprimir los mejores hiperparámetros encontrados
print("Mejores parámetros para SVM: ", grid_search.best_params_)

# Guardar las métricas en el diccionario
metrics_dict['SVC'] = {'Train Accuracy': accuracy_train, 'Test Accuracy': accuracy_test, 'Train Precision': precision_train, 'Test Precision': precision_test, 'Train Recall': recall_train, 'Test Recall': recall_test, 'Train F1-Score': f1_train, 'Test F1-Score': f1_test}




# Definir nombres y clasificadores
names = ['Naive Bayes', 'K-Nearest Neighbors', 'Decision Tree', 'Support Vector Machine']
classifiers = [GaussianNB(), 
               KNeighborsClassifier(n_neighbors=5), 
               DecisionTreeClassifier(max_depth=6, min_samples_leaf=10, min_samples_split=3, random_state=30), 
               SVC(probability=True)]  



plt.figure()

for name, classifier in zip(names, classifiers):
    # Entrenar el clasificador
    classifier.fit(X_train, y_train)

    # Predecir las etiquetas de los datos de prueba
    y_pred = classifier.predict(X_test)

    # Calcular los valores de fpr, tpr y umbral para la curva ROC
    fpr, tpr, _ = roc_curve(y_test, y_pred)

    # Calcular el área bajo la curva ROC (AUC)
    roc_auc = roc_auc_score(y_test, y_pred)

    # Graficar la curva ROC para cada clasificador
    plt.plot(fpr, tpr, label=f'{name} (AUC = {roc_auc:.2f})')

# Configurar y mostrar la gráfica
plt.xlabel('False Positive Rate')
plt.ylabel('True Positive Rate')
plt.title('Curva ROC')
plt.legend()
plt.show()

