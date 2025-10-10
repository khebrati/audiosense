## AudioSense

    Note: This project is currently under active development.

A modern, expressive audiometry application for Android & iOS, built with Kotlin Multiplatform and a focus on user-friendly design powered by Material 3 Expressive.
### 📋 About The Project

AudioSense is a native mobile application for Android & iOS designed to provide users with an accessible and intuitive way to measure their hearing levels. Built from the ground up with Kotlin Multiplatform and Jetpack Compose, the app offers a clean, calming, and user-friendly experience.

The core mission is to translate complex hearing data into meaningful, real-world insights. From a guided test preparation process to understanding your results, every aspect is crafted to be clear and reassuring. The project leverages the Compose Multiplatform Wizard template for a robust and scalable foundation.
### ✨ Key Features
- Accurate Testing: A focused, minimalist interface for conducting pure-tone audiometry tests, with built-in safeguards for a better experience.
- Headphone Calibration: Results are calibrated for different headphone models to ensure higher accuracy.
- Noise Meter: An integrated noise meter helps ensure you're in a sufficiently quiet environment before you begin, preventing inaccurate results.
- Visual & Descriptive Results: View your hearing data on a clear audiogram chart and get useful, easy-to-understand insights about what your results mean in everyday situations.
- Expressive UI: A dynamic and responsive interface built entirely with Jetpack Compose, following Material 3 Expressive Design principles.
- Customization: Settings for theme (Light/Dark/System) and language.

### 🚀 Tech Stack
- Framework: Kotlin Multiplatform (targeting Android & iOS)
- UI: Jetpack Compose / Compose Multiplatform
- Navigation: androidx.navigation
- Dependency Injection: Koin
- Database: Room
- Logging: Kermit

### License

This project is licensed under the MIT License. See the LICENSE file for more details.


# Chapter 7 – Gradients and Initialization


**Slide 2: Introduction**

**Welcome!**

In the previous chapter (Chapter 6), we explored **iterative optimization algorithms** — general-purpose methods for minimizing a function. These start with random initial parameters and make incremental adjustments based on the **gradient of the loss function**, with the goal of finding network parameters that minimize loss and improve prediction accuracy.

In this chapter, we focus on **two essential ingredients** for training deep neural networks successfully:

1. **Gradients** – to determine the direction and magnitude of parameter adjustments.
2. **Initialization** – to ensure training starts from a “healthy” state so learning is stable and efficient.

By the end of this lesson, you will understand:

* How gradients are computed efficiently with **backpropagation**.
* How backpropagation implements the **chain rule** in neural networks.
* Why **variance propagation** through layers is critical for training stability.
* How initialization choices affect learning, and how to prevent **vanishing** and **exploding gradients**.
* How **He** and **Xavier** initialization strategies help keep training on track.

---

**Slide 3: what is Gradients**

A **gradient** measures how much the loss will change if we change a parameter slightly.
A neural network can be viewed as a large equation — often with millions (or even billions) of parameters — so computing all these derivatives efficiently is essential.


The standard approach:

* Use the **chain rule** from calculus.
* Compute gradients in a way that **reuses partial results** instead of recalculating everything from scratch.

This is the essence of **backpropagation**.

---
**Slide 4: Why Backpropagation?**

The derivatives of the loss tell us **how the loss changes** when we make a small change to the parameters.
Optimization algorithms use these derivatives to adjust the parameters so the loss becomes smaller.

The **backpropagation algorithm** computes *all* these derivatives efficiently — instead of recomputing the network for each parameter, it reuses intermediate results from earlier calculations.

We’ll build intuition with **two key observations**.

But before that, let’s recall two math tools that make backpropagation possible.

*Extra Insight:* The backpropagation idea was popularized in the 1980s by **Rumelhart, Hinton, and Williams**, but similar reverse-mode differentiation concepts appeared earlier in control theory.

---

**Slide 5: Mathematical tools for backpro**

1. **Chain Rule:**

$$
\frac{\partial x}{\partial y} = \frac{\partial z}{\partial y} \cdot \frac{\partial x}{\partial z}
$$

This allows us to pass derivatives **backward through layers**.

2. **Matrix Calculus:**

   When parameters are matrices, derivatives become matrices (Jacobians). This is why transposes ($\Omega_k^T$) appear in backprop equations.

Now, let’s build intuition with **two key observations**.

---

**Slide 6: Observation 1 – Forward Pass**

In the **forward pass**, each layer computes:

$$
f_k = \Omega_k h_{k-1} + \beta_k
$$

* $\Omega_k$ = weight matrix
* $\beta_k$ = bias vector
* $h_{k-1}$ = previous layer’s output (or network input)
* $f_k$ = pre-activation

Then applies the activation function:

$$
h_k = g_k(f_k)
$$

We **store all** $h_k$ and $f_k$ — they’re needed later for gradient computation.

---

**Slide 7: Forward Pass Visualization**

*Figure 7.1 – Forward Pass*

As you see, in the forward pass, data flows from input to output:

1. Multiply activations by weights.
2. Add biases.
3. Apply the nonlinearity.
4. Pass results to the next layer.

We store:

* Pre-activations $f_k$
* Activations $h_k$

These stored values are **essential for efficient backpropagation**.

We run the network for each data example in the batch and store these values — this is the **forward pass**.
The stored activations will subsequently be used to compute gradients.

---

**Slide 8: Observation 2 – Backward Pass**

The **backward pass** computes gradients starting from the output layer:

$$
\frac{\partial L}{\partial h_{k-1}} = \Omega_k^\top \frac{\partial L}{\partial f_k}
$$

This propagates **error signals** to earlier layers.

The weight gradient is:

$$
\frac{\partial L}{\partial \Omega_k} = \frac{\partial L}{\partial f_k} h_{k-1}^\top
$$

The bias gradient is:

$$
\frac{\partial L}{\partial \beta_k} = \frac{\partial L}{\partial f_k}
$$

In short: we **pass gradients backward** through the network, using stored forward-pass values to compute parameter gradients.

---

**Slide 9: Backward Pass Visualization**

*Figure 7.2 – Backward Pass*

In the backward pass, **error signals** flow from the output back through the network.
Each layer:

* Receives a gradient from its successor.
* Computes its own parameter gradients.
* Passes an updated gradient to its predecessor.

As we move backward through the network, many required terms have **already been computed** in the previous step — so we reuse them. This is why backpropagation is efficient.

*Extra Insight:*
For example, computing the effect of a small change in weights:

* **Into $h_3$**: Need to know (i) how $h_3$ changes the model output $f$, and (ii) how this output changes the loss $\ell$ (figure 7.2a).
* **Into $h_2$**: Need to know (i) how $h_2$ changes $h_3$, (ii) how $h_3$ changes $f$, and (iii) how $f$ changes the loss (figure 7.2b).
* **Into $h_1$**: Need to know (i) how $h_1$ changes $h_2$, (ii) how $h_2$ changes $h_3$, (iii) how $h_3$ changes $f$, and (iv) how $f$ changes the loss (figure 7.2c).

---

**Slide 10: Toy Example – A Simple Problem**

Before we dive into the full vector/matrix version, let’s consider a **scalar toy problem** to see backpropagation mechanics clearly.

We have a model:

$$
f[x,\phi] = \beta_3 + \omega_3 \cdot \cos\!\big[\beta_2 + \omega_2 \cdot \exp\big(\beta_1 + \omega_1 \cdot \sin(\beta_0 + \omega_0 \cdot x)\big)\big]
$$

where

$$
\phi = \{\beta_0, \omega_0, \beta_1, \omega_1, \beta_2, \omega_2, \beta_3, \omega_3\}
$$

The loss is least squares:

$$
L[\phi] = \sum_i \ell_i, \quad \ell_i = (f[x_i, \phi] - y_i)^2
$$

Goal: compute derivatives of $L$ with respect to each parameter in $\phi$.

* Doing this **manually** is tedious and error-prone.
* Some terms repeat — there’s redundancy.

**Backpropagation** solves this by:

* Starting at the end of the network.
* Working backward.
* Reusing previously computed partial derivatives.

*Figure:* Computation graph of scalar functions — each node stores:

* **Forward value:** the output of that operation.
* **Backward derivative:** the gradient multiplier to pass upstream.

---

**Slide 11: Toy Example – Forward Pass**

We treat loss computation as a **sequence** of simple steps.
For example:

1. $u = \sin(x)$
2. $v = \exp(u)$
3. $y = \cos(v)$

We store **all intermediate values** ($u, v$) during the forward pass — these are needed for the backward pass.



---

**Slide 12: Toy Example – Backward Pass**

Start from the loss derivative $\frac{\partial L}{\partial y}$, then move backward:

1. $\frac{\partial L}{\partial v} = \frac{\partial L}{\partial y} \cdot (-\sin(v))$
2. $\frac{\partial L}{\partial u} = \frac{\partial L}{\partial v} \cdot \exp(u)$
3. $\frac{\partial L}{\partial x} = \frac{\partial L}{\partial u} \cdot \cos(x)$

This is **the chain rule in action**: multiply the incoming gradient by the local derivative.


---

**Slide 13: Backpropagation Step 1 – Forward Pass (Expanded)**

For $k = 1$ to $K$ layers:
a. Compute

$$
\mathbf{f}_k = \Omega_k \mathbf{h}_{k-1} + \beta_k
$$

b. Apply activation:

$$
\mathbf{h}_k = g_k(\mathbf{f}_k)
$$

c. **Store** $\mathbf{f}_k, \mathbf{h}_k$ for the backward pass.

---

**Slide 14: Backpropagation Step 2 – Backward Error Signals (Expanded)**

Start at the output layer $K$:

* Compute $\frac{\partial ℓ}{\partial \mathbf{f}_K}$ from the loss function_this is the starting error signal for the backward pass.

For $k = K$ down to $1$:

$$
\frac{\partial L}{\partial \mathbf{f}_k} = \frac{\partial L}{\partial \mathbf{h}_k} \odot g_k'(\mathbf{f}_k)
$$

($\odot$ means elementwise multiplication)


Compute $\frac{\partial \ell}{\partial \mathbf{f}_K}$ directly from the loss if the last activation is identity, otherwise first compute $\frac{\partial \ell}{\partial \mathbf{h}_K}$ from the loss and then convert to $\frac{\partial \ell}{\partial \mathbf{f}_K} = \frac{\partial \ell}{\partial \mathbf{h}_K} \odot g'_K(\mathbf{f}_K)$ if the last activation is non-identity 



---

**Slide 15: Backpropagation Step 3 – Parameter Gradients (Expanded)**

For each layer $k$:

$$
\frac{\partial L}{\partial \Omega_k} = \frac{\partial L}{\partial \mathbf{f}_k} \ \mathbf{h}_{k-1}^\top \quad
$$

$$
\frac{\partial L}{\partial \beta_k} = \frac{\partial L}{\partial \mathbf{f}_k} \quad   
$$

We use **stored** forward activations $\mathbf{h}_{k-1}$ here.

---

**Slide 16: Backpropagation Step 4 – Batch Aggregation (Expanded)**

For a batch of $B$ examples:

$$
\frac{\partial L}{\partial \Omega_k} = \sum_{b=1}^B \frac{\partial L_b}{\partial \mathbf{f}_k} \ \mathbf{h}_{k-1,b}^\top
$$

We sum per-example gradients to get the **batch gradient**.

*Figure* shows that the final computation of $\frac{\partial \ell_i}{\partial \beta_k}$ and $\frac{\partial \ell_i}{\partial \omega_k}$ is just multiplying $\frac{\partial \ell_i}{\partial f_k}$ by the appropriate partial derivative of $f_k$ with respect to the parameter.

---

**Slide 17 – Backpropagation Algorithm Summary**

1. **Forward pass** – store all activations ($h_k$) and pre-activations ($f_k$).
2. **Backward pass** – compute error signals $\frac{\partial L}{\partial f_k}$ using chain rule and stored values.
3. **Parameter gradients** – compute $\frac{\partial L}{\partial \Omega_k}$ and $\frac{\partial L}{\partial \beta_k}$ using stored forward activations.
4. **Batch aggregation** – sum per-example gradients to form batch updates.

---

**Slide 18 – Algorithmic Differentiation**

Modern frameworks like **PyTorch** and **TensorFlow** use *algorithmic differentiation* to compute gradients automatically.

* Each operation (e.g., ReLU, matrix multiplication) knows its own derivative.
* The framework chains them together during backpropagation — no manual math required!
* This process leverages **GPU parallelism**, speeding up batch processing by treating inputs as **tensors** (e.g., 2D for batches of vectors, 4D for image batches).

**Why this matters for you:**
While understanding backpropagation is essential, you’ll rarely code it from scratch. These tools handle gradients behind the scenes, letting you focus on **model architecture and design**.

💡 **Lecturer question:** “If the framework does all this for you, why should you still understand backpropagation?”

Try it yourself: run a PyTorch model, then inspect `.grad` after training to see the auto-computed derivatives in action!

---

**Slide 20 – Why Initialization matters**

The backpropagation algorithm computes the derivatives that optimization algorithms (SGD, Adam) use to train the model. But **before training begins**, we must decide **how to initialize the parameters**.

**Why initialization matters:**
Consider the pre-activation at layer $k$:

$$
f_k = \beta_k + \Omega_k h_{k-1}, \quad h_k = \mathrm{ReLU}(f_{k})
$$

Two extreme cases:

1. **Too small initialization** ($\sigma^2 \approx 10^{-5}$):

   * Pre-activations shrink layer by layer.
   * ReLU cuts negative values → signal halves each layer.
   * **Result:** Vanishing gradients — updates become negligible, learning stalls.

2. **Too large initialization** ($\sigma^2 \approx 10^{5}$):

   * Pre-activations grow exponentially.
   * **Result:** Exploding gradients — updates become unstable, may overflow.

**In short:**
Poor initialization can cause:

* **Vanishing gradients** – signals shrink each layer.
* **Exploding gradients** – signals grow uncontrollably.

---

**Slide 21 – Variance Propagation: Forward Pass**

Let’s see what happens to signal variance as it moves forward through the network.

Assume $h_{k-1}$ has mean 0, variance $\sigma_{k-1}^2$, weights have variance $\sigma_\omega^2$, and biases are zero.

After the **linear step**:

$$
\mathrm{Var}[f_k] = n_{k-1} \ \sigma_\omega^2 \ \sigma_{k-1}^2
$$

Then the **activation** changes the variance again:

$$
\sigma_k^2 = \mathbb{E}[g(z)^2], \quad z \sim \mathcal{N}(0,\,\mathrm{Var}[f_k])
$$

For ReLU, that’s roughly half the variance.

Our goal is to choose $\sigma_\omega^2$ so $\sigma_k^2$ stays about equal to $\sigma_{k-1}^2$ — that’s how we avoid vanishing or exploding signals.”


---

**Slide 22 – Variance Propagation: Backward Pass**

Now we do the same analysis for the backward pass — how gradient variance changes layer by layer.


Backprop for layer $k$:

$$
\delta_{k-1} = \Omega_k^\top \, \delta_k \odot g'(f_{k-1})
$$

where $\delta_k = \frac{\partial L}{\partial f_k}$ is the error signal at layer $k$.

**the Variance become:**

$$
\mathrm{Var}[\delta_{k-1}] = n_k \ \sigma_\omega^2 \ \mathbb{E}[g'(z)^2] \ \mathrm{Var}[\delta_k]
$$

* $n_k$ = number of outputs of the layer
* $z$ again is Gaussian with the same variance as in the forward pass

Just like in the forward pass, we want this variance to stay constant. If it shrinks → vanishing gradients, if it grows → exploding gradients.”

---

**Slide 23 – Combined Stability Condition**

Okay, so up to now we’ve looked at stability in the forward pass and stability in the backward pass separately.
Now We combine the **forward** and **backward** stability 


* **Forward stability:** keep $\sigma_k^2$ constant → constraint on $\sigma_\omega^2$ using $n_{k-1}$ and $\mathbb{E}[g(z)^2]$.

* **Backward stability:** keep $\mathrm{Var}[\delta_k]$ constant → constraint on $\sigma_\omega^2$ using $n_k$ and $\mathbb{E}[g'(z)^2]$.


if $n_{k-1} = n_k$, those two conditions are identical — easy. But for non-square layers, where the number of inputs and outputs differ, these two formulas give two different answers. We can’t satisfy both exactly.

So what do we do? The book suggests a **compromise formula**: take the average of the two denominators. That gives us the general expression:

$$
\sigma_\Omega^2 = \frac{1}{\mathbb{E}[g'(z)^2] \cdot \frac{n_{k-1} + n_k}{2}}.
$$

For ReLU, $\mathbb{E}[g'(z)^2] = \frac12$


So that’s our balanced choice — it doesn’t perfectly preserve forward or backward variance on its own, but it keeps both reasonably stable for deep networks, even with non-square layers.”


---

**Slide 24 – Transition to Initialization**

We’ve seen the **problem**: poor initialization destabilizes signal flow.
We’ve seen the **solution framework**: choose $\sigma_\Omega^2$ to satisfy variance stability conditions.

**Question:** How do we pick $\sigma_\Omega^2$ for different activation functions in practice?
→ Let’s see how **He** and **Xavier** initialization address this.

---

**Slide 25 – What Do He and Xavier Initialization Do?**

These initialization strategies set the initial weight variance so that:

* **Forward activations** have stable variance across layers.
* **Backward gradients** have stable variance across layers.

They are designed to directly satisfy the conditions we derived for variance preservation.

---

**Slide 26 – Xavier Initialization**

* Best for **mean-zero symmetric activations** like $\tanh$ or logistic sigmoid (in its linear region).
* Formula:

$$
\sigma_\Omega^2 = \frac{1}{n_{k-1}}
$$

* $n_{k-1}$ is number of units in the previous layer. In the book, this may also be written as $D_{h}​$ (layer dimensionality).

* Balances forward and backward variance for symmetric activations where $\mathbb{E}[g(z)^2] \approx 1$.
* From Glorot & Bengio (2010), also called Glorot initialization

---

**Slide 27 – He Initialization**

* Best for **ReLU** and similar activation functions.
* Formula:

$$
\sigma_\Omega^2 = \frac{2}{n_{k-1}}
$$

* For ReLU, $\mathbb{E}[g(z)^2] \approx \frac{1}{2}$, so we **double** the Xavier value to compensate for ReLU halving the activation variance.

* For non-square layers, a compromise formula from the book is:

$$
\sigma_\Omega^2 = \frac{4}{n_{k-1} + n_k}.
$$  

---

**Slide 28 – Comparison of Initializations**

*Figure 7.7 – Variance stability across layers*

As shown in the figure, **He** and **Xavier** maintain variance much better than poor initializations.


On the left, we have the forward variance — the variance of the activations — plotted across 50 layers. On the right, the backward variance — the variance of the gradients — also across the layers.

The setup is a deep network with 50 hidden layers, each having 100 units. Input is a 100-dimensional Gaussian vector with variance 1, the target output is zero, and biases are set to zero. The only thing we vary here is the weight variance at initialization.

Look at the blue curve for $\sigma_\Omega^2 = 0.02$. This corresponds to the He initialization for ReLU with 100 inputs per neuron — and you can see it keeps the variance almost constant through all 50 layers in both directions.

When the variance is smaller than that — like 0.001 or 0.01 — the activations and gradients shrink layer by layer, which is the vanishing signal problem. When the variance is too large — like 0.1 or 1.0 — they blow up exponentially, which is the exploding signal problem.

The big takeaway: proper initialization — here, $\sigma_\Omega^2 = 2/n_{k-1}$ — can dramatically improve stability before training even starts.”


---

**Slide 29 – PyTorch Implementation**

```python
torch.nn.init.kaiming_normal_(layer.weight)  # He initialization
torch.nn.init.xavier_normal_(layer.weight)   # Xavier initialization
```

In most cases, frameworks automatically apply these to common layers, so you rarely have to set them manually.

---------------
Here’s a **summary** and **takeaway** for your full teaching script on Chapter 7 – *Gradients and Initialization*:

---

## **Slide30:Summary and Takeaways**

Backpropagation efficiently computes gradients by reusing computations.

Chain rule + stored activations = fast derivative calculation.

Variance propagation explains vanishing/exploding gradients.

Proper initialization (He/Xavier) preserves stability in forward & backward passes.

Modern frameworks handle gradient computation 
automatically, but understanding the math is essential for:

* Debugging

* Designing architectures

* Choosing initializations




